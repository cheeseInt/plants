package ch.cheese.plants.dto;

import lombok.Data;

@Data
public class Sensor {
    private String id;
    private Boolean has_sensor;
    private Integer status;
    private String version;
    private Double light_factor;
    private Integer probe_length_id;
    private Double moisture_factor;
    private Boolean is_battery_low;
    private String received_data_at;
    private String created_at;
    private Boolean has_sensor_update;
}
