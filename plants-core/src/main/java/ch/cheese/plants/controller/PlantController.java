package ch.cheese.plants.controller;

import ch.cheese.plants.service.PlantImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantImportService plantImportService;

    public PlantController(PlantImportService plantImportService) {
        this.plantImportService = plantImportService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importFromFyta() {
        int count = plantImportService.importPlantsFromFyta();
        return ResponseEntity.ok(count + " Pflanzen erfolgreich importiert.");
    }
}
