package ch.cheese.plants.dto;

import lombok.Data;
import java.util.List;

@Data
public class DeviceActionDto {
    private String action;
    private String title;
    private String description;
    private int priority;
}
