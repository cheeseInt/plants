package ch.cheese.plants.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "care_entries", uniqueConstraints = @UniqueConstraint(columnNames = {"plant_id", "date_utc"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ✅ JPA benötigt genau dieses Feld

    @ManyToOne(optional = false)
    @JoinColumn(name = "plant_id")
    private PlantEntity plant;

    @Column(name = "date_utc", nullable = false)
    private LocalDateTime dateUtc;

    @Column(name = "water_in_liter")
    private BigDecimal waterInLiter;

    @Column(name = "fertilizer_in_ml")
    private Integer fertilizerInMl;
}
