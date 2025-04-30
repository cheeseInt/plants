package ch.cheese.plants.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class SensorEntity {
    @Id
    private String id;
    private Boolean hasSensor;
    private Integer status;
    private String version;
    private Double lightFactor;
    private Integer probeLengthId;
    private Double moistureFactor;
    private Boolean batteryLow;
    private String receivedDataAt;
    private String createdAt;
    private Boolean hasSensorUpdate;
}
