package ch.cheese.plants.fyta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Notifications {
    @JsonProperty("light")
    private boolean light;

    @JsonProperty("temperature")
    private boolean temperature;

    @JsonProperty("water")
    private boolean water;

    @JsonProperty("nutrition")
    private boolean nutrition;

    // Getter und Setter können mit Lombok ersetzt werden
}
