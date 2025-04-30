package ch.cheese.plants.service;

import ch.cheese.plants.dto.Plant;
import ch.cheese.plants.mapper.FytaModelMapperService;
import ch.cheese.plants.repository.PlantRepository;
import ch.cheese.plants.entity.PlantEntity;




import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlantService {

    private final PlantRepository plantRepository;
    private final FytaModelMapperService mapper;

    @Transactional
    public void saveOrUpdatePlant(PlantEntity plantEntity, Plant plantMeasurement) {
        Optional<PlantEntity> existing = plantRepository.findById(plantEntity.getId());

        if (existing.isPresent()) {
            PlantEntity update = existing.get();
            update.setNickname(plantEntity.getNickname());
            update.setScientificName(plantEntity.getScientificName());
            update.setCommonName(plantEntity.getCommonName());
            update.setStatus(plantEntity.getStatus());
            update.setPlantId(plantEntity.getPlantId());
            update.setFamilyId(plantEntity.getFamilyId());
            update.setThumbPath(plantEntity.getThumbPath());
            update.setOriginPath(plantEntity.getOriginPath());
            update.setPlantThumbPath(plantEntity.getPlantThumbPath());
            update.setPlantOriginPath(plantEntity.getPlantOriginPath());
            update.setReceivedDataAt(plantEntity.getReceivedDataAt());
            update.setTemperatureStatus(plantEntity.getTemperatureStatus());
            update.setLightStatus(plantEntity.getLightStatus());
            update.setMoistureStatus(plantEntity.getMoistureStatus());
            update.setSalinityStatus(plantEntity.getSalinityStatus());
            update.setNutrientsStatus(plantEntity.getNutrientsStatus());

            // Nested objects
            update.setFertilisation(plantEntity.getFertilisation());
            update.setNotifications(plantEntity.getNotifications());
            update.setSensor(plantEntity.getSensor());
            update.setHub(plantEntity.getHub());
            update.setGarden(plantEntity.getGarden());
            update.setMeasurements(plantEntity.getMeasurements());
            update.setDeviceMenu(plantEntity.getDeviceMenu());

            plantRepository.save(update);
            log.info("Updated plant with ID {}", plantEntity.getId());
        } else {
            plantRepository.save(plantEntity);
            log.info("Saved new plant with ID {}", plantEntity.getId());
        }
    }
}
