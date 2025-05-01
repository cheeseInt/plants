package ch.cheese.plants.fyta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Garden {
    @JsonProperty("id")
    private int id;

    @JsonProperty("index")
    private String index;

    // Getter und Setter können mit Lombok ersetzt werden
}
