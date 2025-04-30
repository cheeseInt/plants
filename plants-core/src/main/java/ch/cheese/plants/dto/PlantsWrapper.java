package ch.cheese.plants.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlantsWrapper {
    private List<PlantEntry> plants;
}
