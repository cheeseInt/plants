package ch.cheese.plants.fyta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Fertilisation {
    @JsonProperty("last_fertilised_at")
    private String last_fertilised_at;

    @JsonProperty("fertilise_at")
    private String fertilise_at;

    @JsonProperty("was_repotted")
    private boolean was_repotted;

    // Getter und Setter können mit Lombok ersetzt werden
}
