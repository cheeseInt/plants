package ch.cheese.plants.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hubs")
@Data
@NoArgsConstructor
public class HubEntity {
    @Id
    private Integer id;
    private String hubId;
    private String hubName;
    private String version;
    private int status;
    private String receivedDataAt;
    private String reachedHubAt;
}
