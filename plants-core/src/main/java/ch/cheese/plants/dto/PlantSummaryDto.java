package ch.cheese.plants.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlantSummaryDto {
    private Long id;
    private String commonName;
    private String gardenName;
    private String location;
    private String nickname;
    private LocalDateTime receivedDataAt;
    private boolean sensorIsBatteryLow;
    private String thumbPath;
    private String plantThumbPath;
}
