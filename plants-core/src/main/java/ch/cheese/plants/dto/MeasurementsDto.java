package ch.cheese.plants.dto;

import lombok.Data;

@Data
public class MeasurementsDto {
    private Object ph;
    private Object nutrients;
    private Object temperature;
    private Object light;
    private Object moisture;
    private Object salinity;
    private String battery;
}
