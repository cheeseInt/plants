package ch.cheese.plants.controller;

import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.repository.CareEntryRepository;
import ch.cheese.plants.repository.PlantRepository;
import ch.cheese.plants.service.PlantImportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlantController.class)
class PlantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlantRepository plantRepository;

    @MockBean
    private PlantImportService plantImportService;

    @MockBean
    private CareEntryRepository careEntryRepository;

    @Test
    void getPlantSummaries_whenNoPlantsExist_returnsEmptyList() throws Exception {
        when(plantRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/plants/summary"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getPlantSummaries_whenPlantsExist_returnsList() throws Exception {
        PlantEntity plant = new PlantEntity();
        plant.setId(1L);
        plant.setNickname("Ficus");
        when(plantRepository.findAll()).thenReturn(List.of(plant));

        mockMvc.perform(get("/api/plants/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nickname").value("Ficus"));
    }

    @Test
    void deleteCareEntry_whenNotFound_returns404() throws Exception {
        when(careEntryRepository.existsById(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/plants/care/care-entry/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCareEntry_whenFound_returns200() throws Exception {
        when(careEntryRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/plants/care/care-entry/1"))
                .andExpect(status().isOk());
    }

    @Test
    void importFromFyta_withInvalidTimeline_returns400() throws Exception {
        // Timeline.valueOf("INVALID") throws IllegalArgumentException
        // which GlobalExceptionHandler maps to 400
        mockMvc.perform(post("/api/plants/import")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"search\":{\"timeline\":\"INVALID\"}}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void putCareEntry_whenPlantNotFound_returns412() throws Exception {
        when(plantRepository.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/plants/care/care-entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plant_id\":999,\"date_utc\":\"2025-10-22 11:44:54\"}"))
                .andExpect(status().isPreconditionFailed());
    }
}
