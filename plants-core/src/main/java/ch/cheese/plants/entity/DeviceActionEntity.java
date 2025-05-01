package ch.cheese.plants.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class DeviceActionEntity {
    private String action;
    private String title;
    private String description;
    private int priority;
}
