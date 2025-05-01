package ch.cheese.plants.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class NotificationsEmbedded {
    private boolean light;
    private boolean temperature;
    private boolean water;
    private boolean nutrition;
}
