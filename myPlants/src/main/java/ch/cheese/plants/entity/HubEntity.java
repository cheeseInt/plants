package ch.cheese.plants.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class HubEntity {
    @Id
    private Integer id;
    private String hubId;
    private String hubName;
    private String version;
    private Integer status;
    private String receivedDataAt;
    private String reachedHubAt;
}
