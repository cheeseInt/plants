package ch.cheese.plants;

import ch.cheese.plants.dto.CareRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/plants")
@RequiredArgsConstructor
@Slf4j
public class PlantCareController {

    private final PlantCareService plantCareService;
    private final ImportFromFYTA importFromFYTA;

    @GetMapping("/update")
    public ResponseEntity<String> updateFromFyta() {
        try {
            importFromFYTA.importMyPlant();
            return ResponseEntity.ok("FYTA import triggered successfully.");
        } catch (Exception e) {
            log.error("FYTA import failed", e);
            return ResponseEntity.internalServerError().body("FYTA import failed.");
        }
    }

    @PostMapping("/{plantId}/care")
    public ResponseEntity<Void> addCare(@PathVariable Integer plantId, @Valid @RequestBody CareRequest request) {
        try {
            boolean created = plantCareService.addCareEntry(plantId, request);

            if (created) {
                return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
            } else {
                return ResponseEntity.ok().build(); // 200 OK
            }
        } catch (IllegalArgumentException ex) {
            log.error("Invalid request: {}", ex.getMessage());
            return ResponseEntity.badRequest().build(); // 400 Bad Request bei Fehlern
        }
    }
}
