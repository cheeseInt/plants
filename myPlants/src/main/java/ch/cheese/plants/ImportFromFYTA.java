package ch.cheese.plants;

import ch.cheese.plants.dto.Plant;
import ch.cheese.plants.dto.PlantDetailsWrapper;
import ch.cheese.plants.dto.PlantEntry;
import ch.cheese.plants.dto.PlantsWrapper;
import ch.cheese.plants.mapper.FytaModelMapperService;
import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.repository.PlantRepository;
import ch.cheese.plants.service.PlantService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportFromFYTA {

    private final PlantService plantService;
    private final PlantRepository plantRepository;
    private final FytaModelMapperService mapper;
    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String LOGIN_URL = "https://web.fyta.de/api/auth/login";
    private static final String PLANTS_URL = "https://web.fyta.de/api/user-plant";
    private static final String MEASUREMENTS_URL_TEMPLATE = "https://web.fyta.de/api/user-plant/measurements/%d";
    private static final String DETAILS_URL_TEMPLATE = "https://web.fyta.de/api/user-plant/%d";

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

    public void importMyPlant() {
        String accessToken = getAccessToken("cheese_int@me.com", "zurha2-hahrIt-dywzeb");
        if (accessToken == null) {
            log.error("Authentication failed. No access token received.");
            return;
        }

        List<PlantEntry> plantSummaries = getPlantEntries(accessToken);
        if (plantSummaries == null || plantSummaries.isEmpty()) {
            log.warn("No plants found.");
            return;
        }

        for (PlantEntry summary : plantSummaries) {
            PlantEntry fullDetails = getPlantDetails(accessToken, summary.getId());
            Plant plantMeasurement = getPlantMeasurements(accessToken, summary.getId());

            if (fullDetails != null && plantMeasurement != null) {
                PlantEntity entity = new PlantEntity();
                entity.setId(fullDetails.getId());
                entity.setNickname(fullDetails.getNickname());
                entity.setScientificName(fullDetails.getScientific_name());
                entity.setCommonName(fullDetails.getCommon_name());
                entity.setStatus(fullDetails.getStatus());
                entity.setPlantId(fullDetails.getPlant_id());
                entity.setFamilyId(fullDetails.getFamily_id());
                entity.setThumbPath(fullDetails.getThumb_path());
                entity.setOriginPath(fullDetails.getOrigin_path());
                entity.setPlantThumbPath(fullDetails.getPlant_thumb_path());
                entity.setPlantOriginPath(fullDetails.getPlant_origin_path());
                entity.setReceivedDataAt(fullDetails.getReceived_data_at());
                entity.setTemperatureStatus(fullDetails.getTemperature_status());
                entity.setLightStatus(fullDetails.getLight_status());
                    entity.setMoistureStatus(fullDetails.getMoisture_status());
                entity.setSalinityStatus(fullDetails.getSalinity_status());
                entity.setNutrientsStatus(fullDetails.getNutrients_status());

                mapper.enrichPlantEntity(entity, fullDetails);

                plantService.saveOrUpdatePlant(entity, plantMeasurement);
            }
        }
    }

    private String getAccessToken(String email, String password) {
        Map<String, String> body = Map.of("email", email, "password", password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(LOGIN_URL, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("access_token");
            }
        } catch (Exception e) {
            log.error("Login request failed", e);
        }
        return null;
    }

    private List<PlantEntry> getPlantEntries(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<PlantsWrapper> response = restTemplate.exchange(
                    PLANTS_URL, HttpMethod.GET, request, PlantsWrapper.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getPlants();
            }
        } catch (Exception e) {
            log.error("Failed to fetch plant list", e);
        }
        return Collections.emptyList();
    }

    private PlantEntry getPlantDetails(String token, Integer plantId) {
        String url = DETAILS_URL_TEMPLATE.formatted(plantId);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<PlantDetailsWrapper> response = restTemplate.exchange(
                    url, HttpMethod.GET, request, PlantDetailsWrapper.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getPlant();
            }
        } catch (Exception e) {
            log.error("Failed to fetch plant details for ID {}", plantId, e);
        }
        return null;
    }

    private Plant getPlantMeasurements(String token, Integer plantId) {
        String url = MEASUREMENTS_URL_TEMPLATE.formatted(plantId);

        Map<String, Object> body = Map.of(
                "search", Map.of("timeline", "week")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return objectMapper.readValue(response.getBody(), new TypeReference<>() {});
            }
        } catch (Exception e) {
            log.error("Failed to fetch measurements for plant {}", plantId, e);
        }

        return null;
    }
}
