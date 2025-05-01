package ch.cheese.plants.fyta;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FytaAuthService {

    private final WebClient webClient;
    private String accessToken; // lokal gespeicherter Token

    public FytaAuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://web.fyta.de").build();
    }

    public FytaUserPlantsResponse fetchUserPlants() {
        getAccessToken();
        if (accessToken == null) {
            throw new IllegalStateException("No access token available");
        }

        return webClient.get()
                .uri("/api/user-plant")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(FytaUserPlantsResponse.class)
                .block(); // block() nur für synchronen Aufruf
    }

    public FytaPlantDetailResponse fetchUserPlantsDetail(String id) {
        getAccessToken();
        if (accessToken == null) {
            throw new IllegalStateException("No access token available");
        }

        return webClient.get()
                .uri("/api/user-plant/" + id)
                .headers(h -> {
                    h.setBearerAuth(accessToken);
                    h.setContentType(MediaType.APPLICATION_JSON); // <- Wichtig!
                })
                .retrieve()
                .bodyToMono(FytaPlantDetailResponse.class)
                .block();

    }

    public List<FytaMeasurementResponse> fetchMeasurements(String id, String timeline) {
        getAccessToken();
        if (accessToken == null) {
            throw new IllegalStateException("No access token available");
        }
        log.info("Fetching measurements for plant {} with timeline {}", id, timeline);

        webClient.post()
                .uri("https://web.fyta.de/api/user-plant/measurements/" + id)
                .headers(h -> h.setBearerAuth(accessToken))
                .bodyValue(Map.of("search", Map.of("timeline", "week")))
                .exchangeToMono(response -> {
                    // Statuscode loggen
                    log.info("Status: " + response.statusCode());

                    // Body als String loggen
                    return response.bodyToMono(String.class)
                            .doOnNext(body -> log.info("Body:\n" + body));
                })
                .block();


        return webClient.post()
                .uri("/api/user-plant/measurements/" + id)
                .headers(h -> {
                    h.setBearerAuth(accessToken);
                    h.setContentType(MediaType.APPLICATION_JSON); // <- Wichtig!
                })
                .bodyValue("{\"search\": {\"timeline\": \"" + timeline + "\"}}")
                .retrieve()
                .bodyToFlux(FytaMeasurementResponse.class)
                .collectList()
                .block();

    }

    public String loginAndGetAccessToken(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        LoginResponse response = webClient.post()
                .uri("/api/auth/login")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LoginResponse.class)
                .block();

        if (response != null && response.getAccessToken() != null) {
            this.accessToken = response.getAccessToken(); // speichern
            return accessToken;
        }

        return null;
    }

    public String getAccessToken() {
        accessToken = loginAndGetAccessToken("cheese_int@me.com","zurha2-hahrIt-dywzeb");
        if (accessToken == null) {
            throw new IllegalStateException("No access token available");
        }
        return accessToken;
    }

    public boolean hasToken() {
        return accessToken != null;
    }

    public void clearToken() {
        this.accessToken = null;
    }
}
