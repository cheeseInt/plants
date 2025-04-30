package ch.cheese.plants.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "plant_care")
@Data
public class PlantCareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double waterInLiters;
    private Double fertilizerInMl;

    private LocalDateTime dateUtc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    private PlantEntity plant;
}
