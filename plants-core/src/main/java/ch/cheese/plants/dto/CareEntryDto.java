package ch.cheese.plants.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CareEntryDto {
    private Long id;
    private Long plantId;
    private LocalDateTime dateUtc;
    private BigDecimal waterInLiter;
    private Integer fertilizerInMl;
}
