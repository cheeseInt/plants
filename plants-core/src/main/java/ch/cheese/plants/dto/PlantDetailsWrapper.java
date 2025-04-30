package ch.cheese.plants.dto;

import ch.cheese.plants.entity.PlantEntity;
import lombok.Data;

@Data
public class PlantDetailsWrapper {
    private PlantEntity plant;
}
