package ch.cheese.plants.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class PlantMeasurementWrapper {
    private Plant plant;
}
