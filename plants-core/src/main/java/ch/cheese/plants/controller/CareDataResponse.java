package ch.cheese.plants.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CareDataResponse {
    private List<String> importTimelines;
    private List<PlantSummary> plants;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlantSummary {
        private Long id;
        private String nickname;
    }
}
