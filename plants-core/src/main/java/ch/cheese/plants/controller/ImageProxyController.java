package ch.cheese.plants.controller;

import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.fyta.FytaAuthService;
import ch.cheese.plants.repository.PlantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/proxy")
public class ImageProxyController {

    private final PlantRepository plantRepository;
    private final FytaAuthService fytaAuthService;
    private final WebClient webClient;

    public ImageProxyController(
            PlantRepository plantRepository,
            FytaAuthService fytaAuthService,
            WebClient.Builder webClientBuilder) {
        this.plantRepository = plantRepository;
        this.fytaAuthService = fytaAuthService;
        this.webClient = webClientBuilder.build();
    }

    @GetMapping("/thumb")
    public ResponseEntity<byte[]> getImageFromFyta(@RequestParam(name = "id") String id) {
        String token = fytaAuthService.getAccessToken();
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("Fetching image for plant with id {}", id);

        Optional<PlantEntity> optionalPlant = plantRepository.findById(Long.valueOf(id));
        if (optionalPlant.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String url = optionalPlant.get().getThumb_path();
        log.info("Fetching image for plant with url {}", url);

        try {
            byte[] imageBytes = webClient.get()
                    .uri(url)
                    .headers(h -> h.setBearerAuth(token))
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }
}
