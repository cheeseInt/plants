package ch.cheese.plants.fyta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Sensor {
    @JsonProperty("id")
    private String id;

    @JsonProperty("has_sensor")
    private boolean has_sensor;

    @JsonProperty("status")
    private int status;

    @JsonProperty("version")
    private String version;

    @JsonProperty("is_battery_low")
    private boolean is_battery_low;

    @JsonProperty("received_data_at")
    private String received_data_at;

    // Getter und Setter können mit Lombok ersetzt werden
}
