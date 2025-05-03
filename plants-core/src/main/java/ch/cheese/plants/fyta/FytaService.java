package ch.cheese.plants.fyta;


import ch.cheese.plants.config.FytaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Slf4j
@Service
public class FytaService {

    private final WebClient webClient;
    private final FytaProperties fytaProperties;
    private String accessToken; // lokal gespeicherter Token
    private LocalDateTime expiresAt; // lokal gespeicherter Ablauf Datum des Tokens
    private Integer loginCount = 0;

    public FytaService(WebClient.Builder webClientBuilder, FytaProperties fytaProperties, FytaProperties fytaProperties1) {
        this.fytaProperties = fytaProperties1;
        this.webClient = webClientBuilder.baseUrl(fytaProperties.getApiBaseUrl()).build();
    }

    public FytaUserPlantsResponse fetchUserPlants() {
        log.info("Fetching user plants");
        ResponseEntity<FytaUserPlantsResponse> response = webClient.get()
                .uri("/api/user-plant")
                .headers(headers -> headers.setBearerAuth(getAccessToken(false)))
                .retrieve()
                .toEntity(FytaUserPlantsResponse.class)
                .block();

        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else if (response != null && response.getStatusCode() == HttpStatus.FORBIDDEN) {
            log.warn("Access forbidden (403) when fetching user plants");
            log.info("Reload access_token because of HTTP Response (403) when fetching user plants");
            getAccessToken(true);
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
                    h.setBearerAuth(getAccessToken(false));
                    h.setContentType(MediaType.APPLICATION_JSON);
                })
                .retrieve()
                .toEntity(FytaPlantDetailResponse.class)
                .block();

        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else if (response != null && response.getStatusCode() == HttpStatus.FORBIDDEN) {
            log.warn("Access forbidden (403) when fetching plant detail for plant {}", id);
            log.info("Reload access_token because of HTTP Response (403) when fetching user plants");
            getAccessToken(true);
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
                    h.setBearerAuth(getAccessToken(false));
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
            log.info("Reload access_token because of HTTP Response (403) when fetching user plants");
            getAccessToken(true);
            return null;
        }

        log.error("Unexpected response status {} when fetching measurements",
                response != null ? response.getStatusCode() : "null");
        return null;
    }

    private String loginAndGetAccessToken(String email, String password, boolean force) {
        if (!force && accessToken != null && expiresAt != null && expiresAt.isAfter(LocalDateTime.now())) {
            return accessToken; // Token ist noch gültig
        }
        log.info("Logging in with email {} and password {}", email, password);
        LoginRequest request = new LoginRequest(email, password);
        loginCount++;
        if(loginCount > 3) {
            log.error("Too many login attempts. Aborting login");
            throw new IllegalStateException("Too many login attempts");
        }
        try {
            ResponseEntity<LoginResponse> response = webClient.post()
                    .uri("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .toEntity(LoginResponse.class)
                    .block();

            if (response == null) {
                log.error("No response received from login endpoint");
                return null;
            }

            HttpStatusCode statusCode = response.getStatusCode();

            if (statusCode == HttpStatus.UNAUTHORIZED) {
                log.error("Login failed: unauthorized (401) for email {}", email);
                this.accessToken = null;
                return null;
            }

            if (statusCode != HttpStatus.OK) {
                log.error("Login failed: unexpected response status {} for email {}", statusCode, email);
                this.accessToken = null;
                return null;
            }

            LoginResponse loginResponse = response.getBody();
            if (loginResponse == null || loginResponse.getAccessToken() == null) {
                log.error("Login response was successful, but access token is missing");
                this.accessToken = null;
                return null;
            }
            this.accessToken = loginResponse.getAccessToken();
            this.expiresAt = LocalDateTime.now().plusSeconds(loginResponse.getExpiresIn()).minusDays(2);
            log.info("Login successful. Token expires at {}", this.expiresAt);
            return accessToken;

        } catch (Exception e) {
            log.error("Login failed due to exception for email {}", email, e);
            this.accessToken = null;
            return null;
        }
    }

    public String getAccessToken(boolean force) {
        accessToken = loginAndGetAccessToken(fytaProperties.getAuth().getEmail(),fytaProperties.getAuth().getPassword(), force);
        if (accessToken == null) {
            throw new IllegalStateException("No access token available");
        }
        return accessToken;
    }
}
