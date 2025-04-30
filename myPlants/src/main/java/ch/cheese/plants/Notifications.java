package ch.cheese.plants;

import lombok.Data;

@Data
public class Notifications {
    private boolean light;
    private boolean temperature;
    private boolean water;
    private boolean nutrition;
}
