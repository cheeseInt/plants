package ch.cheese.plants.dto;

import lombok.Data;
import java.util.List;

@Data
public class Device_menuDto {
    private List<String> actions;
    private List<String> buttons;
    private Boolean live_mode_enabled;
    private Boolean diagnose_enabled;
}
