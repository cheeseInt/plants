package ch.cheese.careui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Arrays;
import ch.cheese.careui.model.PlantEntity;

@Controller
public class PlantViewController {

    private final RestTemplate restTemplate;

    public PlantViewController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/plants")
    public String showPlants(Model model) {
        ResponseEntity<PlantEntity[]> response = restTemplate.getForEntity(
                "http://localhost:8081/api/plants", PlantEntity[].class);
        List<PlantEntity> plants = Arrays.asList(response.getBody());
        model.addAttribute("plants", plants);
        return "plants";
    }
}
