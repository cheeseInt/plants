package ch.cheese.plants.service;

import ch.cheese.plants.entity.MeasurementEntity;
import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.fyta.*;
import ch.cheese.plants.mapper.MeasurementMapper;
import ch.cheese.plants.mapper.PlantMapper;
import ch.cheese.plants.repository.MeasurementRepository;
import ch.cheese.plants.repository.PlantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlantImportService {

    private final FytaAuthService fytaAuthService;
    private final PlantMapper plantMapper;
    private final MeasurementMapper measurementMapper;
    private final PlantRepository plantRepository;
    private final MeasurementRepository measurementRepository;


    public PlantImportService(FytaAuthService fytaAuthService, PlantMapper plantMapper, MeasurementMapper measurementMapper, PlantRepository plantRepository, MeasurementRepository measurementRepository) {
        this.fytaAuthService = fytaAuthService;
        this.plantMapper = plantMapper;
        this.measurementMapper = measurementMapper;
        this.plantRepository = plantRepository;
        this.measurementRepository = measurementRepository;
    }

    private List<Plant> plantList;
    private List<PlantEntity> plantEntityList;
    private List<MeasurementEntity> measurementEntityList;

    public List<PlantEntity> getAllPlants() {
        return plantRepository.findAll();
    }
    public void importMeasurements() {

        // measurement
        for (Plant plant : plantList) {
            log.info("Fetching measurements for plant {}", plant.getId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            FytaMeasurementWrapper wrapper = fytaAuthService.fetchMeasurements(String.valueOf(plant.getId()), "month");
            List<FytaMeasurementResponse> measurements = wrapper.getMeasurements();
            if (measurements == null) {
                log.warn("No measurements for plant {}", plant.getId());
            } else if (measurements.isEmpty()) {
                log.warn("No measurements for plant {} found", plant.getId());
            } else {
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
            }
        }
    }

    public void importPlantDetails() {
        // plantsDetail
        int imported = 0;
        log.info("Starting to import details from {} plants", plantList.size());
        for (Plant plant : plantList) {
            if (!plantRepository.existsById((long) plant.getId())) {
                PlantEntity entity = plantMapper.toEntity(plant);
                // Hole Detaildaten
                FytaPlantDetailResponse detail = fytaAuthService.fetchUserPlantsDetail(String.valueOf(plant.getId()));
                // erweitere Entity mit Detaildaten (nur leere Felder)
                plantMapper.updateEntityWithDetails(entity, detail);
                // speichere in die db
                log.info("Saving plant {} with details", plant.getId());
                plantRepository.save(entity);
                imported++;
            }
        }
        log.info("Imported {} plants from Fyta", imported);

    }

    public void importPlants() {
        // plants
        FytaUserPlantsResponse response = fytaAuthService.fetchUserPlants();
        plantList = response.getPlants();
        log.info("Fetched {} plants from Fyta", plantList.size());

        plantEntityList = plantList.stream()
                .map(plantMapper::toEntity)
                .collect(Collectors.toList());
        log.info("Mapped {} plants to entities", plantEntityList.size());
        plantRepository.saveAll(plantEntityList);
        log.info("Saved {} plants to db", plantEntityList.size());

    }


    public int importPlantsFromFyta() {
        importPlants();
        importPlantDetails();
        importMeasurements();
        return plantEntityList.size();
    }
}
