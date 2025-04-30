package ch.cheese.plants.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeviceMenu {
    private List<Action> actions;
    private List<String> buttons;
    private Boolean live_mode_enabled;
    private Boolean diagnose_enabled;
}
