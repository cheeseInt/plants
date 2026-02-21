package ch.cheese.plants.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Data;
import java.time.LocalDateTime;

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
    private LocalDateTime received_data_at;

    @JsonProperty("reached_hub_at")
    private LocalDateTime reached_hub_at;
}
