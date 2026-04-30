package ch.cheese.plants.service;

import ch.cheese.plants.config.Timeline;
import ch.cheese.plants.entity.BatteryLogEntity;
import ch.cheese.plants.entity.MeasurementEntity;
import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.fyta.*;
import ch.cheese.plants.mapper.MeasurementMapper;
import ch.cheese.plants.mapper.PlantMapper;
import ch.cheese.plants.repository.BatteryLogRepository;
import ch.cheese.plants.repository.MeasurementRepository;
import ch.cheese.plants.repository.PlantRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlantImportService {

    private final FytaService fytaService;
    private final PlantMapper plantMapper;
    private final MeasurementMapper measurementMapper;
    private final PlantRepository plantRepository;
    private final MeasurementRepository measurementRepository;
    private final BatteryLogRepository batteryLogRepository;

    public PlantImportService(FytaService fytaService, PlantMapper plantMapper, MeasurementMapper measurementMapper, PlantRepository plantRepository, MeasurementRepository measurementRepository, BatteryLogRepository batteryLogRepository) {
        this.fytaService = fytaService;
        this.plantMapper = plantMapper;
        this.measurementMapper = measurementMapper;
        this.plantRepository = plantRepository;
        this.measurementRepository = measurementRepository;
        this.batteryLogRepository = batteryLogRepository;
    }

    private List<Plant> plantList;

    @Cacheable("plants")
    public List<PlantEntity> getAllPlants() {
        return plantRepository.findAll();
    }

    public void importMeasurements(Timeline timeline) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int succeeded = 0;
        int failed = 0;
        for (Plant plant : plantList) {
            try {
                importMeasurementsForPlant(plant, timeline, formatter);
                succeeded++;
            } catch (Exception e) {
                failed++;
                log.error("Failed to import measurements for plant {} ({}): {}",
                        plant.getId(), plant.getNickname(), e.getMessage(), e);
            }
        }
        log.info("Measurement import done: {} plants ok, {} failed", succeeded, failed);
    }

    private void importMeasurementsForPlant(Plant plant, Timeline timeline, DateTimeFormatter formatter) {
        log.info("Fetching measurements for plant {}", plant.getId());
        FytaMeasurementWrapper wrapper = fytaService.fetchMeasurements(String.valueOf(plant.getId()), timeline.toString());
        if (wrapper == null) {
            log.warn("No measurement data returned for plant {}, skipping", plant.getId());
            return;
        }
        List<FytaMeasurementResponse> measurements = wrapper.getMeasurements();
        if (measurements == null || measurements.isEmpty()) {
            log.warn("No measurements found for plant {}", plant.getId());
            return;
        }
        int saved = 0;
        for (FytaMeasurementResponse dto : measurements) {
            log.info("Measurement for plant {} date {} temp {}", plant.getId(), dto.getDate_utc(), dto.getTemperature());
            LocalDateTime date = LocalDateTime.parse(dto.getDate_utc(), formatter);
            boolean exists = measurementRepository.findByPlantIdAndDateUtc((long) plant.getId(), date).isPresent();
            if (!exists) {
                MeasurementEntity entity = measurementMapper.toEntity(dto, (long) plant.getId());
                measurementRepository.save(entity);
                saved++;
                log.info("Saved measurement for plant {} at {}", plant.getId(), date);
            } else {
                log.info("Measurement for plant {} at {} already exists", plant.getId(), date);
            }
        }
        log.info("Saved {} new measurements for plant {}", saved, plant.getId());
    }

    public void importPlantDetails() {
        int imported = 0;
        int failed = 0;
        log.info("Starting to import details from {} plants", plantList.size());
        for (Plant plant : plantList) {
            try {
                importPlantDetail(plant);
                imported++;
            } catch (Exception e) {
                failed++;
                log.error("Failed to import details for plant {} ({}): {}",
                        plant.getId(), plant.getNickname(), e.getMessage(), e);
            }
        }
        log.info("Imported {} plants from Fyta ({} failed)", imported, failed);
    }

    private void importPlantDetail(Plant plant) {
        PlantEntity entity = plantMapper.toEntity(plant);
        FytaPlantDetailResponse detail = fytaService.fetchUserPlantsDetail(String.valueOf(plant.getId()));
        if (detail == null) {
            log.warn("No detail data returned for plant {}, skipping", plant.getId());
            return;
        }
        plantMapper.updateEntityWithDetails(entity, detail);
        log.info("Saving plant {} with details", plant.getId());
        plantRepository.save(entity);

        BatteryLogEntity batteryLogEntity = new BatteryLogEntity();
        batteryLogEntity.setPlant(entity);
        List<FytaPlantDetailResponse.SensorInfo> sensors = detail.getPlant().getSensors();
        Integer battery = sensors == null ? null : sensors.stream()
                .map(FytaPlantDetailResponse.SensorInfo::getBattery_level)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        if (battery == null) {
            log.warn("No battery level available yet for plant {}, skipping battery log", plant.getId());
            return;
        }
        batteryLogEntity.setBattery(battery);
        batteryLogEntity.setDateUtc(LocalDateTime.now(ZoneOffset.UTC));
        batteryLogRepository.save(batteryLogEntity);
        log.info("Saved battery log for plant {}", plant.getId());
    }

    @Transactional
    public void importPlants() {
        FytaUserPlantsResponse response = fytaService.fetchUserPlants();
        if (response == null || response.getPlants() == null) {
            log.error("Failed to fetch plants from Fyta, aborting import");
            return;
        }
        plantList = response.getPlants();
        log.info("Fetched {} plants from Fyta", plantList.size());

        List<PlantEntity> plantEntityList = plantList.stream()
                .map(plantMapper::toEntity)
                .collect(Collectors.toList());
        log.info("Mapped {} plants to entities", plantEntityList.size());
        plantRepository.saveAll(plantEntityList);
        log.info("Saved {} plants to db", plantEntityList.size());
    }

    @CacheEvict(value = "plants", allEntries = true)
    public void importPlantsFromFyta(Timeline timeline) {
        importPlants();
        if (plantList == null || plantList.isEmpty()) {
            log.error("No plants loaded, aborting import");
            return;
        }
        importPlantDetails();
        importMeasurements(timeline);
    }
}