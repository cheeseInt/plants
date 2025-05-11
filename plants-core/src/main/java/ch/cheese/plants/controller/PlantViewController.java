package ch.cheese.plants.controller;

import ch.cheese.plants.entity.PlantCareEntryEntity;
import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.fyta.FytaService;
import ch.cheese.plants.repository.PlantCareRepository;
import ch.cheese.plants.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PlantViewController {

    private final PlantRepository plantRepository;
    private final PlantCareRepository plantCareRepository;
    private final ZoneId localZone = ZoneId.of("Europe/Zurich");

    @PostMapping("/plants/care/update")
    public String updateCareEntry(@RequestParam("id") Long id,
                                  @RequestParam("waterInLiter") BigDecimal waterInLiter,
                                  @RequestParam("fertilizerInMl") Integer fertilizerInMl) {

        Optional<PlantCareEntryEntity> entryOpt = plantCareRepository.findById(id);
        if (entryOpt.isPresent()) {
            PlantCareEntryEntity entry = entryOpt.get();
            entry.setWaterInLiter(waterInLiter);
            entry.setFertilizerInMl(fertilizerInMl);
            plantCareRepository.save(entry);
        }

        return "redirect:/plants/care";
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

        model.addAttribute("plants", plants);
        model.addAttribute("entries", entries);
        model.addAttribute("searchNickname", nickname);
        model.addAttribute("searchDate", date);
        return "plants";
    }

    @GetMapping("/plants")
    public String showPlants(Model model) {
        model.addAttribute("plants", plantRepository.findAll());
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
