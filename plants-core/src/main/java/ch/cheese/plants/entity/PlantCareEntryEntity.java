package ch.cheese.plants.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "plant_care")
@Data
public class PlantCareEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "plant_id")
    private PlantEntity plant;

    @Column(name = "care_time", nullable = false)
    private LocalDateTime careTime;

    @Column(name = "water_in_liter")
    private BigDecimal waterInLiter;

    @Column(name = "fertilizer_in_ml")
    private Integer fertilizerInMl;
}
