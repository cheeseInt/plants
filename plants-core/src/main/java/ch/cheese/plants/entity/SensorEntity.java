package ch.cheese.plants.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sensors")
@Data
@NoArgsConstructor
public class SensorEntity {
    @Id
    private String id;
    private boolean hasSensor;
    private int status;
    private String version;
    private double lightFactor;
    private int probeLengthId;
    private double moistureFactor;
    private boolean batteryLow;
    private String receivedDataAt;
    private String createdAt;
    private boolean sensorUpdate;
}
