package ch.cheese.plants.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class HubEntity {

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
}
