package ch.cheese.plants.controller;

import ch.cheese.plants.dto.*;
import ch.cheese.plants.mapper.FytaModelMapperService;
import ch.cheese.plants.service.PlantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlantController {

    private final PlantService plantService;
    private final FytaModelMapperService fytaModelMapperService;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String LOGIN_URL = "https://web.fyta.de/api/auth/login";
    private static final String PLANTS_URL = "https://web.fyta.de/api/user-plant";
    private static final String MEASUREMENTS_URL_TEMPLATE = "https://web.fyta.de/api/user-plant/measurements/";
    private static final String DETAILS_URL_TEMPLATE = "https://web.fyta.de/api/user-plant/";
    String accessToken = getAccessToken("cheese_int@me.com", "zurha2-hahrIt-dywzeb");

    public void importMyPlant(String timeline) {
        log.info("🌿 Starte Import mit timeline: {}", timeline);

        String accessToken = getAccessToken("cheese_int@me.com", "zurha2-hahrIt-dywzeb");
        if (accessToken == null) {
            log.error("Authentication failed. No access token received.");
            return;
        }
        log.info("🔐 Access Token geholt");

        List<PlantEntry> plantSummaries = getPlantEntries(accessToken);
        if (plantSummaries == null || plantSummaries.isEmpty()) {
            log.warn("No plants found.");
            return;
        }
        log.info("🌱 {} Pflanzen geladen", plantSummaries.size());

        for (PlantEntry summary : plantSummaries) {

            try {
                log.info("➡️ Importiere Pflanze ID {}", summary.getId());

                PlantDetailsWrapper details = getPlantDetails(String.valueOf(summary.getId()), accessToken);
                if (details == null) {
                    log.warn("⚠️ Details nicht gefunden für Pflanze {}", summary.getId());
                    continue;
                }

                Plant measurement = getPlantMeasurements(String.valueOf(summary.getId()), timeline, accessToken);
                if (measurement == null) {
                    log.warn("⚠️ Messungen nicht gefunden für Pflanze {}", summary.getId());
                    continue;
                }

                plantService.saveOrUpdatePlant(details.getPlant(), measurement);

                log.info("✅ Pflanze {} erfolgreich importiert", summary.getId());

            } catch (Exception e) {
                log.error("❌ Fehler beim Import der Pflanze ID {}: {}", summary.getId(), e.getMessage(), e);
            }
        }

        log.info("🚀 Import abgeschlossen");
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

    public PlantDetailsWrapper getPlantDetails(String plantId, String accessToken) {
        String url = DETAILS_URL_TEMPLATE + plantId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        log.info("Fetching plant details for ID {}", plantId);
        log.info("Fetching plant details on {}", url);
        log.info("token {}", accessToken);
        log.info("header {}", headers );


        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<PlantDetailsWrapper> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    PlantDetailsWrapper.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("❌ Failed to fetch plant details for ID {}: {}", plantId, e.getMessage(), e);
            return null;
        }
    }

    public PlantMeasurementsResponse getPlantMeasurements(String plantId, String timeline, String accessToken) {
        String url = MEASUREMENTS_URL_TEMPLATE + plantId;

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
            log.error("❌ Failed to fetch plant measurements for ID {}: {}", plantId, e.getMessage(), e);
            return null;
        }
    }
}
