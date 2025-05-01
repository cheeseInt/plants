package ch.cheese.plants.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class NotificationsEntity {

    @JsonProperty("light")
    private Boolean light;

    @JsonProperty("temperature")
    private Boolean temperature;

    @JsonProperty("water")
    private Boolean water;

    @JsonProperty("nutrition")
    private Boolean nutrition;
}
