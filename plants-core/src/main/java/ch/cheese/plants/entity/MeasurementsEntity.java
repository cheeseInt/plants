package ch.cheese.plants.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import lombok.Data;

@Data
@Embeddable
public class MeasurementsEntity {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "ph_type")),
            @AttributeOverride(name = "status", column = @Column(name = "ph_status")),
            @AttributeOverride(name = "values", column = @Column(name = "ph_values")),
            @AttributeOverride(name = "unit", column = @Column(name = "ph_unit")),
            @AttributeOverride(name = "absolute_values", column = @Column(name = "ph_absolute_values"))
    })
    private MeasurementValueEntity ph;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "nutrients_type")),
            @AttributeOverride(name = "status", column = @Column(name = "nutrients_statuss")),
            @AttributeOverride(name = "values", column = @Column(name = "nutrients_values")),
            @AttributeOverride(name = "unit", column = @Column(name = "nutrients_unit")),
            @AttributeOverride(name = "absolute_values", column = @Column(name = "nutrients_absolute_values"))
    })
    private MeasurementValueEntity nutrients;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "temperature_type")),
            @AttributeOverride(name = "status", column = @Column(name = "temperature_statuss")),
            @AttributeOverride(name = "values", column = @Column(name = "temperature_values")),
            @AttributeOverride(name = "unit", column = @Column(name = "temperature_units")),
            @AttributeOverride(name = "absolute_values", column = @Column(name = "temperature_absolute_values"))
    })
    private MeasurementValueEntity temperature;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "light_type")),
            @AttributeOverride(name = "status", column = @Column(name = "light_statuss")),
            @AttributeOverride(name = "values", column = @Column(name = "light_values")),
            @AttributeOverride(name = "unit", column = @Column(name = "light_unit")),
            @AttributeOverride(name = "absolute_values", column = @Column(name = "light_absolute_values"))
    })
    private MeasurementValueEntity light;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "moisture_type")),
            @AttributeOverride(name = "status", column = @Column(name = "moisture_statuss")),
            @AttributeOverride(name = "values", column = @Column(name = "moisture_values")),
            @AttributeOverride(name = "unit", column = @Column(name = "moisture_unit")),
            @AttributeOverride(name = "absolute_values", column = @Column(name = "moisture_absolute_values"))
    })
    private MeasurementValueEntity moisture;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "salinity_type")),
            @AttributeOverride(name = "status", column = @Column(name = "salinity_statuss")),
            @AttributeOverride(name = "values", column = @Column(name = "salinity_values")),
            @AttributeOverride(name = "unit", column = @Column(name = "salinity_unit")),
            @AttributeOverride(name = "absolute_values", column = @Column(name = "salinity_absolute_values"))
    })
    private MeasurementValueEntity salinity;

    private String battery;
}
