package ch.cheese.plants.controller;

import ch.cheese.plants.config.Timeline;
import ch.cheese.plants.dto.PlantSummaryDto;
import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.repository.PlantRepository;
import ch.cheese.plants.service.PlantImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final PlantRepository plantRepository;
    private final PlantImportService plantImportService;

    public PlantController(
            PlantImportService plantImportService, PlantRepository plantRepository
    ) {
        this.plantImportService = plantImportService;
        this.plantRepository = plantRepository;
    }

    @GetMapping("/summary")
    public List<PlantSummaryDto> getPlantSummaries() {
        return plantRepository.findAll().stream()
                .map(plant -> new PlantSummaryDto(
                        plant.getId(),
                        plant.getCommon_name(),
                        "todo",
                        plant.getLocation(),
                        plant.getNickname(),
                        LocalDateTime.parse(plant.getReceived_data_at(), formatter),
                        false,
                        plant.getThumb_path(),
                        plant.getPlant_thumb_path()
                ))
                .collect(Collectors.toList());
//                plant.getGarden() != null ? plant.getGarden().getName() : null,
//                plant.isSensor_is_battery_low(),
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
