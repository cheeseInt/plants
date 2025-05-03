package ch.cheese.plants.controller;

import ch.cheese.plants.config.Timeline;
import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.fyta.FytaAuthService;
import ch.cheese.plants.service.PlantImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantImportService plantImportService;

    public PlantController(
            PlantImportService plantImportService
    ) {
        this.plantImportService = plantImportService;
    }


    @PostMapping("/import")
    public ResponseEntity<Void> importFromFyta(@RequestBody Map<String, Map<String, String>> body) {
        String timelineStr = body.getOrDefault("search", Collections.emptyMap()).getOrDefault("timeline", "day");
        Timeline timeline = Timeline.valueOf(timelineStr.toUpperCase());
        log.info("Importing plants from Fyta with timeline {}", timeline);
        plantImportService.importPlantsFromFyta(timeline);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PlantEntity>> getAllPlants() {
        return ResponseEntity.ok(plantImportService.getAllPlants());
    }

}
