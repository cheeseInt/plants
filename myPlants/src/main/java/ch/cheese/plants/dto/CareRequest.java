package ch.cheese.plants.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CareRequest {

    @NotNull(message = "Water amount must not be null")
    @PositiveOrZero(message = "Water amount must be zero or positive")
    private Double waterInLiters;

    @NotNull(message = "Fertilizer amount must not be null")
    @PositiveOrZero(message = "Fertilizer amount must be zero or positive")
    private Double fertilizerInMl;

    @NotNull(message = "Date must not be null")
    private String date; // <-- Neu! Format "dd.MM.yyyy"
}
