package ch.cheese.plants.dto;

import lombok.Data;

@Data
public class Notifications {
    private Boolean light;
    private Boolean temperature;
    private Boolean water;
    private Boolean nutrition;
}
