package ch.cheese.plants.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class MeasurementDto {
    private double light;
    private double temperature;
    private double soil_moisture;
    private boolean soil_moisture_anomaly;
    private double soil_fertility;
    private boolean soil_fertility_anomaly;
    private long thresholds_id;
    private String date_utc;
}
