package ch.cheese.plants.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class FertilisationEmbedded {
    private String lastFertilisedAt;
    private String fertiliseAt;
    private boolean wasRepotted;
}
