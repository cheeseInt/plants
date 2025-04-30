package ch.cheese.plants.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DeviceMenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String rawJson; // optional serialized view
}
