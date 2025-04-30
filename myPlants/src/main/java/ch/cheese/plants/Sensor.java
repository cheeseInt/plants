package ch.cheese.plants;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sensor {
    private String id;
    private boolean has_sensor;
    private int status;
    private String version;
    private boolean is_battery_low;
    private String received_data_at;
}
