package ch.cheese.plants;

import lombok.Data;

import java.util.List;

@Data
public class PlantsWrapper {
    private List<PlantEntry> plants;
}
