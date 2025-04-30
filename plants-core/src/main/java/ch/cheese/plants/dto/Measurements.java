package ch.cheese.plants.dto;

import lombok.Data;

@Data
public class Measurements {
    private Battery battery;
    private SensorMeasurement ph;
    private SensorMeasurement nutrients;
    private SensorMeasurement temperature;
    private SensorMeasurement light;
    private SensorMeasurement moisture;
    private SensorMeasurement salinity;
}
