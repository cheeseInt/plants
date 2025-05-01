package ch.cheese.plants.fyta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Hub {
    @JsonProperty("id")
    private int id;

    @JsonProperty("hub_id")
    private String hub_id;

    @JsonProperty("hub_name")
    private String hub_name;

    @JsonProperty("version")
    private String version;

    @JsonProperty("status")
    private int status;

    @JsonProperty("received_data_at")
    private String received_data_at;

    @JsonProperty("reached_hub_at")
    private String reached_hub_at;

    // Getter und Setter können mit Lombok ersetzt werden
}
