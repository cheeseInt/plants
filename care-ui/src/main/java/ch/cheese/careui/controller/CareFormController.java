package ch.cheese.careui.controller;

import ch.cheese.careui.dto.CareForm;
import ch.cheese.careui.entity.PlantShortView;
import ch.cheese.careui.repository.PlantRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
@Slf4j
public class CareFormController {

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/care")
    public String showForm(Model model) {
        List<PlantShortView> plants = plantRepository.findAll();
        model.addAttribute("plants", plants);
        model.addAttribute("careForm", new CareForm());
        return "care-form";
    }

    @PostMapping("/care")
    public String submitForm(@Valid @ModelAttribute CareForm careForm,
                             BindingResult result,
                             Model model) {
        List<PlantShortView> plants = plantRepository.findAll();
        model.addAttribute("plants", plants);

        if (result.hasErrors()) return "care-form";

        // 💡 ID über nickname nachschlagen
        var optional = plantRepository.findByNickname(careForm.getNickname());
        if (optional.isEmpty()) {
            model.addAttribute("error", "Pflanze nicht gefunden");
            return "care-form";
        }
        Integer plantId = optional.get().getId();

        String url = "http://localhost:8055/plants/" + plantId + "/care";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CareForm> request = new HttpEntity<>(careForm, headers);

        try {
            restTemplate.postForEntity(url, request, String.class);
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "care-form";
    }
}
