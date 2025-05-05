package ch.cheese.plants.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CareEntryRequest {
    @JsonProperty("plant_id")
    private Long plantId;

    @JsonProperty("date_utc")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateUtc;

    @JsonProperty("water_in_liter")
    private BigDecimal waterInLiter;

    @JsonProperty("fertilizer_in_ml")
    private Integer fertilizerInMl;
}
