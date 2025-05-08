package ch.cheese.plants.controller;

import ch.cheese.plants.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PlantViewController {

    private final PlantRepository plantRepository;

    @GetMapping("/plants")
    public String showPlants(Model model) {
        model.addAttribute("plants", plantRepository.findAll());
        return "plants"; // → templates/plants.html
    }
}
