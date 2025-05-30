package ch.cheese.plants.controller;

import ch.cheese.plants.config.Timeline;
import ch.cheese.plants.entity.PlantCareEntryEntity;
import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.repository.PlantCareRepository;
import ch.cheese.plants.repository.PlantRepository;
import ch.cheese.plants.service.PlantImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PlantViewController {

    private final PlantRepository plantRepository;
    private final PlantCareRepository plantCareRepository;
    private final PlantImportService plantImportService;
    private final ZoneId localZone = ZoneId.of("Europe/Zurich");

    @PostMapping("/plants/import")
    public String importPlants(@RequestParam("timeline") Timeline timeline, Model model) {
        try {
            plantImportService.importPlantsFromFyta(timeline);
            model.addAttribute("importResponse", "200 OK – Import erfolgreich");
        } catch (Exception e) {
            model.addAttribute("importResponse", "500 ERROR – " + e.getMessage());
        }

        // für Dropdown-Werte + Seite reload
        model.addAttribute("plants", plantRepository.findAll());
        model.addAttribute("timelines", Arrays.stream(Timeline.values()).map(Enum::name).toList());
        return "plants";
    }

    @GetMapping("/plants/care")
    public String viewCareEntries(@RequestParam(name = "nickname", required = false) String nickname,
                                  @RequestParam(name = "date", required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                  Model model) {

        List<PlantEntity> plants = plantRepository.findAll();
        List<PlantCareEntryEntity> entries = plantCareRepository.findAll().stream()
                .filter(entry -> nickname == null || nickname.isBlank() || entry.getPlant().getNickname().equalsIgnoreCase(nickname))
                .filter(entry -> date == null || entry.getCareTime().toLocalDate().isEqual(date))
                .toList();

        // TODO - check if this is correct
        for (PlantCareEntryEntity entry : entries) {
            if (entry.getCareTime() != null) {
                entry.setCareTime(LocalDateTime.ofInstant(entry.getCareTime().toInstant(ZoneOffset.UTC), localZone));
            }
        }

        model.addAttribute("plants", plants);
        model.addAttribute("entries", entries);
        model.addAttribute("searchNickname", nickname);
        model.addAttribute("searchDate", date);
        model.addAttribute("timelines", Arrays.stream(Timeline.values()).map(Enum::name).toList());
        return "plants";
    }

    @GetMapping("/plants")
    public String showPlants(Model model) {
        model.addAttribute("plants", plantRepository.findAll());
        model.addAttribute("timelines", Arrays.stream(Timeline.values()).map(Enum::name).toList());
        return "plants";
    }

    @PostMapping("/plants/care")
    public String saveCareEntry(@RequestParam("plantId") Long plantId,
                                @RequestParam("careTime")
                                @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime careTime,
                                @RequestParam("waterInLiter") BigDecimal waterInLiter,
                                @RequestParam("fertilizerInMl") Integer fertilizerInMl) {

        Optional<PlantEntity> plantOpt = plantRepository.findById(plantId);
        if (plantOpt.isPresent()) {
            // In ZonedDateTime umwandeln
            ZonedDateTime zonedDateTime = careTime.atZone(localZone);

            // In UTC konvertieren
            ZonedDateTime utcDateTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);

            PlantCareEntryEntity entry = new PlantCareEntryEntity();
            entry.setPlant(plantOpt.get());
            entry.setCareTime(utcDateTime.toLocalDateTime());
            entry.setWaterInLiter(waterInLiter);
            entry.setFertilizerInMl(fertilizerInMl);
            plantCareRepository.save(entry);
        }

        return "redirect:/plants";
    }
}
