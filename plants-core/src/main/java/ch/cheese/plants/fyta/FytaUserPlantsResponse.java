package ch.cheese.plants.fyta;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FytaUserPlantsResponse {

    private List<Plant> plants;

    // optional: nested DTOs wie Garden oder Hub kannst du später hinzufügen
}
