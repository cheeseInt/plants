package ch.cheese.plants.controller;

import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.fyta.FytaAuthService;
import ch.cheese.plants.service.PlantImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantImportService plantImportService;
    private final FytaAuthService fytaAuthService;
    private final WebClient webClient;

    public PlantController(
            PlantImportService plantImportService,
            FytaAuthService fytaAuthService,
            WebClient webClient
    ) {
        this.plantImportService = plantImportService;
        this.fytaAuthService = fytaAuthService;
        this.webClient = webClient;
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
