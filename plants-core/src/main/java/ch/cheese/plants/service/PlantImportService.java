package ch.cheese.plants.service;

import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.fyta.FytaAuthService;
import ch.cheese.plants.fyta.FytaPlantDetailResponse;
import ch.cheese.plants.fyta.Plant;
import ch.cheese.plants.fyta.FytaUserPlantsResponse;
import ch.cheese.plants.mapper.PlantMapper;
import ch.cheese.plants.repository.PlantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlantImportService {

    private final FytaAuthService fytaAuthService;
    private final PlantMapper plantMapper;
    private final PlantRepository plantRepository;

    public PlantImportService(FytaAuthService fytaAuthService, PlantMapper plantMapper, PlantRepository plantRepository) {
        this.fytaAuthService = fytaAuthService;
        this.plantMapper = plantMapper;
        this.plantRepository = plantRepository;
    }

    public List<PlantEntity> getAllPlants() {
        return plantRepository.findAll();
    }

    public int importPlantsFromFyta() {
        FytaUserPlantsResponse response = fytaAuthService.fetchUserPlants();
        List<Plant> plants = response.getPlants();
        log.info("Fetched {} plants from Fyta", plants.size());

        List<PlantEntity> entities = plants.stream()
                .map(plantMapper::toEntity)
                .collect(Collectors.toList());
        log.info("Mapped {} plants to entities", entities.size());
        plantRepository.saveAll(entities);
        log.info("Saved {} plants to db", entities.size());

        int imported = 0;
        log.info("Starting to import details from {} plants", plants.size());
        for (Plant plant : plants) {
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
        return entities.size();
    }
}
