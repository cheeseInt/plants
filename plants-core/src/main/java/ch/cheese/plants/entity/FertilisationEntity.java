package ch.cheese.plants.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class FertilisationEntity {

    @JsonProperty("last_fertilised_at")
    private String last_fertilised_at;

    @JsonProperty("fertilise_at")
    private String fertilise_at;

    @JsonProperty("was_repotted")
    private Boolean was_repotted;
}
