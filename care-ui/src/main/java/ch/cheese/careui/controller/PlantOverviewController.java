package ch.cheese.careui.controller;

import ch.cheese.careui.dto.PlantSummaryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Controller
public class PlantOverviewController {

    private final WebClient webClient;

    @Value("${plants-core.base-url}")
    private String plantsCoreBaseUrl;

    @Value("${care-ui.base-url}")
    private String careUiBaseUrl;

    public PlantOverviewController(WebClient.Builder builder) {
        log.info("Initializing PlantOverviewController with care-ui-base-url {}", careUiBaseUrl);
        this.webClient = builder.baseUrl("http://localhost:8081").build(); // evtl. env-basiert
    }

    @GetMapping("/plants-overview")
    public String showPlants(Model model) {
        List<PlantSummaryDto> plants = webClient.get()
                .uri("/api/plants/summary")
                .retrieve()
                .bodyToFlux(PlantSummaryDto.class)
                .collectList()
                .block();

        if (plants == null) {
            log.error("No plants found");
            // Handle null case appropriately
            plants = new ArrayList<>(); // or return an error page
        }

        model.addAttribute("plants", plants);
        log.info("Model attribute plantsCoreBaseUrl: {}", plantsCoreBaseUrl);
        model.addAttribute("plantsCoreBaseUrl", "http://localhost:8080");


        return "plants-overview";
    }
}
