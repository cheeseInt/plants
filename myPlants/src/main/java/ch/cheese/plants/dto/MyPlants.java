package ch.cheese.plants.dto;

import lombok.Data;
import java.util.List;

@Data
public class MyPlants {
    private List<Garden> gardens;
    private List<PlantEntry> plants;
    private PlantOverview plantOverview;
    private List<Object> hubs_with_lost_connection; // not really used now
}
