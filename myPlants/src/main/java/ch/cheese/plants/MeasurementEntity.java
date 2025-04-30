package ch.cheese.plants;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "measurements")
@Data
public class MeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Automatische ID für jede Messung

    private Integer light;
    private Integer temperature;
    private Integer soilMoisture;
    private Boolean soilMoistureAnomaly;
    private Double soilFertility;
    private Boolean soilFertilityAnomaly;
    private Integer thresholdsId;
    private LocalDateTime dateUtc; // Datum/Uhrzeit der Messung (UTC String)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    private PlantEntity plant; // Verknüpfung zur Pflanze
}
