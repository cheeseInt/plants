package ch.cheese.plants.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gardens")
@Data
@NoArgsConstructor
public class GardenEntity {
    @Id
    private Integer id;
    private String name;
}
