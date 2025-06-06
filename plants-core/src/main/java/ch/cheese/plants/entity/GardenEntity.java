package ch.cheese.plants.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class GardenEntity {

    @JsonProperty("id")
    private int id;

    @JsonProperty("index")
    private String index;
}
