package ch.cheese.plants.controller;

import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.fyta.FytaAuthService;
import ch.cheese.plants.service.PlantImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantImportService plantImportService;
    private final FytaAuthService fytaAuthService;
    private final WebClient webClient;

    public PlantController(PlantImportService plantImportService, FytaAuthService fytaAuthService, WebClient.Builder webClientBuilder) {
        this.plantImportService = plantImportService;
        this.fytaAuthService = fytaAuthService;
        this.webClient = webClientBuilder.build();
    }

    @GetMapping("/api/proxy/plant-thumb/{id}")
    public ResponseEntity<byte[]> proxyThumbImage(@PathVariable Long id, @RequestParam String timestamp) {
        String token = fytaAuthService.getAccessToken(); // gespeicherter Token
        String url = "https://api.prod.fyta-app.de/user-plant/" + id + "/thumb_path?timestamp=" + timestamp;

        byte[] image = webClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(byte[].class)
                .block();

        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(image);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importFromFyta() {
        int count = plantImportService.importPlantsFromFyta();
        return ResponseEntity.ok(count + " Pflanzen erfolgreich importiert.");
    }

    @GetMapping
    public ResponseEntity<List<PlantEntity>> getAllPlants() {
        return ResponseEntity.ok(plantImportService.getAllPlants());
    }

}
