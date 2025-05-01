package ch.cheese.plants.dto;

import lombok.Data;

@Data
public class SensorDto {
    private String id;
    private boolean has_sensor;
    private int status;
    private String version;
    private double light_factor;
    private int probe_length_id;
    private double moisture_factor;
    private boolean is_battery_low;
    private String received_data_at;
    private String created_at;
    private boolean has_sensor_update;
}
