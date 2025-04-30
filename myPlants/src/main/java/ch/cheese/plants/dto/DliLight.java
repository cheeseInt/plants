package ch.cheese.plants.dto;

import lombok.Data;

@Data
public class DliLight {
    private double dli_light;
    private String date_utc;
    private int thresholds_id;
}
