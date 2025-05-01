package ch.cheese.plants.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.List;

@Data
@Embeddable
public class Device_menuEntity {
    @ElementCollection
    private List<DeviceActionEntity> actions;
    private List<String> buttons;
    private Boolean live_mode_enabled;
    private Boolean diagnose_enabled;
}
