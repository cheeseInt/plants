package ch.cheese.plants.service;

import ch.cheese.plants.dto.Plant;
import ch.cheese.plants.dto.PlantDetailsWrapper;
import ch.cheese.plants.dto.PlantMeasurementsResponse;
import ch.cheese.plants.dto.MeasurementDto;
import ch.cheese.plants.entity.HubEntity;
import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.entity.MeasurementEntity;
import ch.cheese.plants.repository.*;
import ch.cheese.plants.mapper.FytaModelMapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlantService {

    private final RestTemplate restTemplate;
    private final PlantRepository plantRepository;
    private final HubRepository hubRepository;
    private final GardenRepository gardenRepository;
    private final SensorRepository sensorRepository;
    private final MeasurementRepository measurementRepository;
    private final FytaModelMapperService fytaModelMapperService;

    public String getAccessToken() {
        String loginUrl = "https://web.fyta.de/api/auth/login";

        Map<String, String> credentials = Map.of(
                "email", "cheese_int@me.com",
                "password", "zurha2-hahrIt-dywzeb"
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(
                loginUrl,
                credentials,
                Map.class
        );

        return (String) response.getBody().get("access_token");
    }

    public List<Integer> getPlantEntries(String accessToken) {
        String url = "https://web.fyta.de/api/user-plant";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<PlantDetailsWrapper[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                PlantDetailsWrapper[].class
        );

        return List.of(response.getBody()).stream()
                .map(wrapper -> wrapper.getPlant().getId())
                .collect(Collectors.toList());
    }

    public Plant getPlantDetails(String plantId, String accessToken) {
        String url = "https://web.fyta.de/api/user-plant/" + plantId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class
        );

        return fytaModelMapperService.mapPlant(response.getBody());
    }

    public PlantMeasurementsResponse getPlantMeasurements(String plantId, String timeline, String accessToken) {
        String url = "https://web.fyta.de/api/user-plant/measurements/" + plantId;

        Map<String, Object> body = Map.of(
                "search", Map.of("timeline", timeline)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<PlantMeasurementsResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    PlantMeasurementsResponse.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("❌ Failed to fetch measurements for ID {}: {}", plantId, e.getMessage(), e);
            return null;
        }
    }

    public void saveOrUpdatePlant(Plant plant, PlantMeasurementsResponse measurements) {
        log.info("💾 Speichere Pflanze {} in DB", plant.getId());

        // Pflanze speichern oder aktualisieren
        final PlantEntity plantEntity = fytaModelMapperService.mapToEntity(plant);

        if (plantEntity.getGarden() != null) {
            gardenRepository.findById(plantEntity.getGarden().getId())
                    .ifPresentOrElse(
                            plantEntity::setGarden,
                            () -> plantEntity.setGarden(gardenRepository.save(plantEntity.getGarden())));
        }

        if (plantEntity.getSensor() != null) {
            sensorRepository.findById(plantEntity.getSensor().getId())
                    .ifPresentOrElse(
                            plantEntity::setSensor,
                            () -> plantEntity.setSensor(sensorRepository.save(plantEntity.getSensor())));
        }

        if (plantEntity.getHub() != null) {
            hubRepository.findById(plantEntity.getHub().getId())
                    .ifPresentOrElse(
                            plantEntity::setHub,
                            () -> plantEntity.setHub(hubRepository.save(plantEntity.getHub())));
        }

        PlantEntity entity = plantRepository.save(plantEntity);


//
//        // existierende Messungen zu dieser Pflanze holen
//        Set<LocalDateTime> existingDates = measurementRepository.findByPlantId(plantEntity.getId()).stream()
//                .map(MeasurementEntity::getDateUtc)
//                .collect(Collectors.toSet());
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        List<MeasurementEntity> newMeasurements = new ArrayList<>();
//        if (measurements.getMeasurements() != null) {
//            for (MeasurementDto m : measurements.getMeasurements()) {
//                LocalDateTime date = LocalDateTime.parse(m.getDate_utc(), formatter);
//                if (existingDates.contains(date)) {
//                    continue;
//                }
//                MeasurementEntity entity = new MeasurementEntity();
//                entity.setPlant(plantEntity);
//                entity.setDateUtc(date);
//                entity.setLight(m.getLight());
//                entity.setTemperature(m.getTemperature());
//                entity.setSoilMoisture(m.getSoil_moisture());
//                entity.setSoilFertility(m.getSoil_fertility());
//                entity.setSoilMoistureAnomaly(m.isSoil_moisture_anomaly());
//                entity.setSoilFertilityAnomaly(m.isSoil_fertility_anomaly());
//                entity.setThresholdsId(m.getThresholds_id());
//                newMeasurements.add(entity);
//            }
//        }
//
//        measurementRepository.saveAll(newMeasurements);
//        log.info("✅ {} neue Messungen gespeichert für Pflanze {}", newMeasurements.size(), plant.getId());
    }
}
