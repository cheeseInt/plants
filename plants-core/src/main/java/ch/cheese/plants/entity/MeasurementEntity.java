package ch.cheese.plants.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "measurements", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"plant_id", "date_utc"})
})
public class MeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plant_id", nullable = false)
    private Long plantId;

    private int light;
    private int temperature;

    @Column(name = "soil_moisture")
    private int soilMoisture;

    @Column(name = "soil_moisture_anomaly")
    private Boolean soilMoistureAnomaly;

    @Column(name = "soil_fertility")
    private int soilFertility;

    @Column(name = "soil_fertility_anomaly")
    private Boolean soilFertilityAnomaly;

    @Column(name = "thresholds_id")
    private int thresholdsId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_utc", nullable = false)
    private LocalDateTime dateUtc;
}
