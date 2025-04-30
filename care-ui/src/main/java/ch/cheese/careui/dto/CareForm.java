package ch.cheese.careui.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CareForm {

    @NotNull
    private Long plantId;

    @NotNull
    private String nickname;

    @NotNull
    @DecimalMin("0.0")
    private Double waterInLiters;

    @NotNull
    @DecimalMin("0.0")
    private Double fertilizerInMl;

    @NotBlank
    private String date; // Format: yyyy-MM-dd (HTML-Datepicker)

}
