package ch.cheese.plants.dto;

import lombok.Data;

import java.util.Map;

@Data
public class SensorMeasurement {
    private String type;
    private Integer status;
    private Map<String, String> values;
    private Map<String, String> absolute_values;
    private Map<String, String> dli_values;
    private String unit;
    private String dli_unit;
}
