package ch.cheese.plants.dto;

import lombok.Data;
import java.util.List;

@Data
public class Device_menuDto {
    private List<String> actions;
    private List<String> buttons;
    private boolean live_mode_enabled;
    private boolean diagnose_enabled;
}
