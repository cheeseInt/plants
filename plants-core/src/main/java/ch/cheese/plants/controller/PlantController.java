package ch.cheese.plants.controller;

import ch.cheese.plants.config.Timeline;
import ch.cheese.plants.dto.CareEntryDto;
import ch.cheese.plants.dto.CareEntryRequest;
import ch.cheese.plants.dto.PlantSummaryDto;
import ch.cheese.plants.entity.PlantCareEntryEntity;
import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.repository.CareEntryRepository;
import ch.cheese.plants.repository.PlantRepository;
import ch.cheese.plants.service.PlantImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ch.cheese.plants.dto.CareEntryQueryRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final PlantRepository plantRepository;
    private final PlantImportService plantImportService;
    private final CareEntryRepository careEntryRepository;

    public PlantController(
            PlantImportService plantImportService, PlantRepository plantRepository, CareEntryRepository careEntryRepository
    ) {
        this.plantImportService = plantImportService;
        this.plantRepository = plantRepository;
        this.careEntryRepository = careEntryRepository;
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

    @PostMapping("/care/care-entries/query")
    public ResponseEntity<List<CareEntryDto>> queryCareEntries(@RequestBody CareEntryQueryRequest request) {
        List<PlantCareEntryEntity> entries;

        if (request.getPlantId() != null && request.getFromDate() != null) {
            entries = careEntryRepository.findByPlant_IdAndCareTimeAfter(request.getPlantId(), request.getFromDate());
        } else if (request.getPlantId() != null) {
            entries = careEntryRepository.findByPlant_Id(request.getPlantId());
        } else if (request.getFromDate() != null) {
            entries = careEntryRepository.findByCareTimeAfter(request.getFromDate());
        } else {
            entries = careEntryRepository.findAll();
        }

        List<CareEntryDto> dtoList = entries.stream().map(entry -> {
            CareEntryDto dto = new CareEntryDto();
            dto.setId(entry.getId());
            dto.setPlantId(entry.getPlant().getId());
            dto.setDateUtc(entry.getCareTime());
            dto.setWaterInLiter(entry.getWaterInLiter());
            dto.setFertilizerInMl(entry.getFertilizerInMl());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    @DeleteMapping("/care/care-entry/{id}")
    public ResponseEntity<String> deleteCareEntry(@PathVariable Long id) {
        if (!careEntryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        careEntryRepository.deleteById(id);
        return ResponseEntity.ok("deleted");
    }

    @PutMapping("/care/care-entry")
    public ResponseEntity<String> putCareEntry(@RequestBody CareEntryRequest request) {
        try {
            Optional<PlantEntity> plantOpt = plantRepository.findById(request.getPlantId());
            if (plantOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Plant not found");
            }

            PlantEntity plant = plantOpt.get();
            Optional<PlantCareEntryEntity> optionalEntry = careEntryRepository
                    .findByPlant_IdAndCareTime(request.getPlantId(), request.getDateUtc());

            if (optionalEntry.isPresent()) {
                PlantCareEntryEntity existing = optionalEntry.get();

                boolean changed = false;

                if (request.getWaterInLiter() != null && !request.getWaterInLiter().equals(existing.getWaterInLiter())) {
                    existing.setWaterInLiter(request.getWaterInLiter());
                    changed = true;
                }

                if (request.getFertilizerInMl() != null && !request.getFertilizerInMl().equals(existing.getFertilizerInMl())) {
                    existing.setFertilizerInMl(request.getFertilizerInMl());
                    changed = true;
                }

                if (changed) {
                    careEntryRepository.save(existing);
                    return ResponseEntity.noContent().build(); // 204
                } else {
                    return ResponseEntity.ok("No changes"); // 200
                }

            } else {
                PlantCareEntryEntity newEntry = new PlantCareEntryEntity();
                newEntry.setPlant(plant);
                newEntry.setCareTime(request.getDateUtc());
                newEntry.setWaterInLiter(request.getWaterInLiter());
                newEntry.setFertilizerInMl(request.getFertilizerInMl());
                careEntryRepository.save(newEntry);
                return ResponseEntity.status(HttpStatus.CREATED).body("Created"); // 201
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/care/data")
    public ResponseEntity<CareDataResponse> getCareData() {
        List<String> timelines = Arrays.stream(Timeline.values())
                .map(Timeline::toString)
                .collect(Collectors.toList());

        List<CareDataResponse.PlantSummary> plantSummaries = plantRepository.findAll().stream()
                .map(p -> new CareDataResponse.PlantSummary(p.getId(), p.getNickname()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new CareDataResponse(timelines, plantSummaries));
    }


    @GetMapping
    public ResponseEntity<List<PlantEntity>> getAllPlants() {
        return ResponseEntity.ok(plantImportService.getAllPlants());
    }

}
