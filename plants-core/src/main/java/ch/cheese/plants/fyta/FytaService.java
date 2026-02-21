package ch.cheese.plants.fyta;


import ch.cheese.plants.config.FytaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class FytaService {

    private final WebClient webClient;
    private final FytaProperties fytaProperties;

    public FytaService(WebClient.Builder webClientBuilder, FytaProperties fytaProperties) {
        this.fytaProperties = fytaProperties;
        this.webClient = webClientBuilder.baseUrl(fytaProperties.getApiBaseUrl()).build();
    }

    public FytaUserPlantsResponse fetchUserPlants() {
        log.info("Fetching user plants from {}", fytaProperties.getApiBaseUrl());
        ResponseEntity<FytaUserPlantsResponse> response = webClient.get()
                .uri("/api/user-plant")
                .headers(headers -> headers.setBearerAuth(getAccessToken()))
                .retrieve()
                .toEntity(FytaUserPlantsResponse.class)
                .block();

        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else if (response != null && response.getStatusCode() == HttpStatus.FORBIDDEN) {
            log.warn("Access forbidden (403) when fetching user plants");
            return null;
        }
        log.warn("Unexpected response status {} when fetching user plants",
                response != null ? response.getStatusCode() : "null");
        return null;
    }

    public FytaPlantDetailResponse fetchUserPlantsDetail(String id) {
        log.info("Fetching plant detail for plant {}", id);

        ResponseEntity<FytaPlantDetailResponse> response = webClient.get()
                .uri("/api/user-plant/" + id)
                .headers(h -> {
                    h.setBearerAuth(getAccessToken());
                    h.setContentType(MediaType.APPLICATION_JSON);
                })
                .retrieve()
                .toEntity(FytaPlantDetailResponse.class)
                .block();

        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else if (response != null && response.getStatusCode() == HttpStatus.FORBIDDEN) {
            log.warn("Access forbidden (403) when fetching plant detail for plant {}", id);
            return null;
        }

        log.warn("Unexpected response status {} when fetching plant detail",
                response != null ? response.getStatusCode() : "null");
        return null;
    }

    public FytaMeasurementWrapper fetchMeasurements(String id, String timeline) {
        log.info("Fetching measurements for plant {} with timeline {}", id, timeline);

        ResponseEntity<FytaMeasurementWrapper> response = webClient.post()
                .uri("/api/user-plant/measurements/" + id)
                .headers(h -> {
                    h.setBearerAuth(getAccessToken());
                    h.setContentType(MediaType.APPLICATION_JSON);
                })
                .bodyValue("{\"search\": {\"timeline\": \"" + timeline + "\"}}")
                .retrieve()
                .toEntity(FytaMeasurementWrapper.class)
                .block();

        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else if (response != null && response.getStatusCode() == HttpStatus.FORBIDDEN) {
            log.error("Access forbidden (403) when fetching measurements for plant {}", id);
            return null;
        }

        log.error("Unexpected response status {} when fetching measurements",
                response != null ? response.getStatusCode() : "null");
        return null;
    }

    public String getAccessToken() {
        String token = fytaProperties.getAccessToken();
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("No access token configured. Set FYTA_ACCESS_TOKEN env variable.");
        }
        return token;
    }
}