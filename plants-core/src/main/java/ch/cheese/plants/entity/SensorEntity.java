package ch.cheese.plants.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class SensorEntity {

    @JsonProperty("id")
    private String id;

    @JsonProperty("has_sensor")
    private Boolean has_sensor;

    @JsonProperty("status")
    private int status;

    @JsonProperty("version")
    private String version;

    @JsonProperty("is_battery_low")
    private Boolean is_battery_low;

    @JsonProperty("received_data_at")
    private String received_data_at;
}
