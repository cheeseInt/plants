package ch.cheese.plants.fyta;


import ch.cheese.plants.config.FytaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;

@Slf4j
@Service
public class FytaService {

    /** Re-login a bit before the token actually expires to avoid racing the expiry. */
    private static final Duration EXPIRY_BUFFER = Duration.ofMinutes(5);

    private final WebClient webClient;
    private final FytaProperties fytaProperties;

    private volatile String cachedToken;
    private volatile Instant tokenExpiry;

    public FytaService(WebClient.Builder webClientBuilder, FytaProperties fytaProperties) {
        this.fytaProperties = fytaProperties;
        this.webClient = webClientBuilder.baseUrl(fytaProperties.getApiBaseUrl()).build();
    }

    public FytaUserPlantsResponse fetchUserPlants() {
        log.info("Fetching user plants from {}", fytaProperties.getApiBaseUrl());
        return withAuthRetry(token -> webClient.get()
                .uri("/api/user-plant")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(FytaUserPlantsResponse.class)
                .block());
    }

    public FytaPlantDetailResponse fetchUserPlantsDetail(String id) {
        log.info("Fetching plant detail for plant {}", id);
        return withAuthRetry(token -> webClient.get()
                .uri("/api/user-plant/" + id)
                .headers(h -> {
                    h.setBearerAuth(token);
                    h.setContentType(MediaType.APPLICATION_JSON);
                })
                .retrieve()
                .bodyToMono(FytaPlantDetailResponse.class)
                .block());
    }

    public FytaMeasurementWrapper fetchMeasurements(String id, String timeline) {
        log.info("Fetching measurements for plant {} with timeline {}", id, timeline);
        return withAuthRetry(token -> webClient.post()
                .uri("/api/user-plant/measurements/" + id)
                .headers(h -> {
                    h.setBearerAuth(token);
                    h.setContentType(MediaType.APPLICATION_JSON);
                })
                .bodyValue("{\"search\": {\"timeline\": \"" + timeline + "\"}}")
                .retrieve()
                .bodyToMono(FytaMeasurementWrapper.class)
                .block());
    }

    /**
     * Executes an authenticated FYTA call. On a 403 the cached token is dropped and the
     * call is retried once with a freshly obtained token, covering the case where a token
     * was revoked before its advertised expiry.
     */
    private <T> T withAuthRetry(Function<String, T> call) {
        try {
            return call.apply(getAccessToken());
        } catch (WebClientResponseException.Forbidden e) {
            log.warn("Received 403 from FYTA, re-authenticating and retrying once");
            invalidateToken();
            return call.apply(getAccessToken());
        }
    }

    public String getAccessToken() {
        String token = cachedToken;
        if (token != null && tokenExpiry != null && Instant.now().isBefore(tokenExpiry)) {
            return token;
        }
        return login();
    }

    /**
     * Logs in against the FYTA API with the configured email/password and caches the
     * returned access token together with its expiry. Synchronized so concurrent callers
     * only trigger a single login round-trip.
     */
    private synchronized String login() {
        // Another thread may have refreshed the token while we waited on the lock.
        String token = cachedToken;
        if (token != null && tokenExpiry != null && Instant.now().isBefore(tokenExpiry)) {
            return token;
        }

        String email = fytaProperties.getEmail();
        String password = fytaProperties.getPassword();
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new IllegalStateException(
                    "No FYTA credentials configured. Set FYTA_EMAIL and FYTA_PASSWORD env variables.");
        }

        log.info("Logging in to FYTA API as {}", email);
        LoginResponse response = webClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new LoginRequest(email, password))
                .retrieve()
                .bodyToMono(LoginResponse.class)
                .block();

        if (response == null || response.getAccessToken() == null || response.getAccessToken().isBlank()) {
            throw new IllegalStateException("FYTA login did not return an access token");
        }

        cachedToken = response.getAccessToken();
        tokenExpiry = Instant.now().plusSeconds(response.getExpiresIn()).minus(EXPIRY_BUFFER);
        log.info("Obtained FYTA access token, valid until {}", tokenExpiry);
        return cachedToken;
    }

    /** Drops the cached token so the next call re-authenticates (e.g. after a 403). */
    private void invalidateToken() {
        cachedToken = null;
        tokenExpiry = null;
    }
}