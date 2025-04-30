package ch.cheese.plants.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class GardenEntity {
    @Id
    private Integer id;
    private String name;
}
