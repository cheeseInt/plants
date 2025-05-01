package ch.cheese.plants.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class MeasurementValueEntity {
    private String type;
    private int status;
    private String values;
    private String unit;
    private String absolute_values;
}
