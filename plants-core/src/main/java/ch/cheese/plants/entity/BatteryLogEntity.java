package ch.cheese.plants.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "battery_log")
@Data
public class BatteryLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plant_id")
    private PlantEntity plant;

    @Column(name = "battery", nullable = false)
    private Integer battery;

    @Column(name = "date_utc", nullable = false)
    private LocalDateTime dateUtc;
}
