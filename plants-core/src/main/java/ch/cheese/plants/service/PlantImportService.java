package ch.cheese.plants.service;

import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.fyta.FytaAuthService;
import ch.cheese.plants.fyta.Plant;
import ch.cheese.plants.fyta.FytaUserPlantsResponse;
import ch.cheese.plants.mapper.PlantMapper;
import ch.cheese.plants.repository.PlantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

        List<PlantEntity> entities = plants.stream()
                .map(plantMapper::toEntity)
                .collect(Collectors.toList());

        plantRepository.saveAll(entities);

        return entities.size();
    }
}
