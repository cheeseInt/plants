package ch.cheese.plants.dto;

import lombok.Data;

@Data
public class AbsoluteValues {
    private Range light;
    private Range dli_light;
    private Range temperature;
    private Range soil_moisture;
    private Range soil_fertility;}
