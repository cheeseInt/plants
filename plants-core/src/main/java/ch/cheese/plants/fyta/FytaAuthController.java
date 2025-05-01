package ch.cheese.plants.fyta;

import ch.cheese.plants.service.PlantImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fyta")
public class FytaAuthController {

    private final PlantImportService plantImportService;
    private final FytaAuthService fytaAuthService;

    public FytaAuthController(PlantImportService plantImportService, FytaAuthService fytaAuthService) {
        this.plantImportService = plantImportService;
        this.fytaAuthService = fytaAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String accessToken = fytaAuthService.loginAndGetAccessToken(request.getEmail(), request.getPassword());
        if (accessToken != null) {
            return ResponseEntity.ok(accessToken);
        } else {
            return ResponseEntity.status(401).body("Login failed or access token not received");
        }
    }

    @GetMapping("/token")
    public ResponseEntity<String> getToken() {
        if (fytaAuthService.hasToken()) {
            return ResponseEntity.ok(fytaAuthService.getAccessToken());
        } else {
            return ResponseEntity.status(404).body("No token stored");
        }
    }

    @DeleteMapping("/token")
    public ResponseEntity<Void> clearToken() {
        fytaAuthService.clearToken();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/plants/import")
    public ResponseEntity<String> importPlants() {
        plantImportService.importPlantsFromFyta();
        return ResponseEntity.ok("Import abgeschlossen");
    }

    @GetMapping("/plants")
    public ResponseEntity<FytaUserPlantsResponse> getUserPlants() {
        try {
            FytaUserPlantsResponse response = fytaAuthService.fetchUserPlants();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

}
