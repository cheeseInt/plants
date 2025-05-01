package ch.cheese.plants.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "measurements")
@Data
@NoArgsConstructor
public class MeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private PlantEntity plant;

    @Column(name = "date_utc", nullable = false)
    private LocalDateTime dateUtc;

    private Double light;
    private Double temperature;

    @Column(name = "soil_moisture")
    private Double soilMoisture;

    @Column(name = "soil_fertility")
    private Double soilFertility;

    @Column(name = "soil_moisture_anomaly")
    private Boolean soilMoistureAnomaly;

    @Column(name = "soil_fertility_anomaly")
    private Boolean soilFertilityAnomaly;

    @Column(name = "thresholds_id")
    private Long thresholdsId;
}
