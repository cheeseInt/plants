package ch.cheese.plants;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private static final String LOGIN_URL = "https://web.fyta.de/api/auth/login";
    private static final String PLANTS_URL = "https://web.fyta.de/api/user-plant";
    private static final String MEASUREMENTS_URL_TEMPLATE = "https://web.fyta.de/api/user-plant/measurements/%d";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public void importMyPlant() {
        String accessToken = getAccessToken("cheese_int@me.com", "zurha2-hahrIt-dywzeb");
        if (accessToken == null) {
            log.error("Authentication failed. No access token received.");
            return;
        }

        List<PlantEntry> plantEntries = getPlantEntries(accessToken);
        if (plantEntries == null || plantEntries.isEmpty()) {
            log.warn("No plants found.");
            return;
        }

        Map<PlantEntry, Plant> plantData = new HashMap<>();
        for (PlantEntry entry : plantEntries) {
            Plant plant = getPlantMeasurements(accessToken, entry.getId());
            if (plant != null) {
                plantData.put(entry, plant);
            }
        }

        plantService.savePlants(plantData);
        log.info("Imported {} plants with measurements", plantData.size());
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
            log.error("Failed to fetch measurements for plant " + plantId, e);
        }

        return null;
    }
}
