package ch.cheese.plants;

import lombok.Data;

@Data
public class Measurement {
    private int light;
    private int temperature;
    private int soil_moisture;
    private boolean soil_moisture_anomaly;
    private double soil_fertility;
    private boolean soil_fertility_anomaly;
    private int thresholds_id;
    private String date_utc;
}

