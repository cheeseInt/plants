package ch.cheese.plants.fyta;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(FytaPlantDetailResponse.class)
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
