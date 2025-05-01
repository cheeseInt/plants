package ch.cheese.plants.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "plants")
@Data
public class PlantEntity {

    @Id
    private Long id;

    private String nickname;
    private String scientific_name;
    private String common_name;
    private int status;
    private int plant_id;
    private String family_id;
    private boolean is_shared;

    @Lob
    @Column(name = "peers_json", columnDefinition = "TEXT")
    private String peersJson;

    private String index;
    private int wifi_status;
    private String thumb_path;
    private String origin_path;
    private String plant_thumb_path;
    private String plant_origin_path;
    private String received_data_at;
    private int temperature_optimal_hours;
    private int light_optimal_hours;
    private boolean eligibility;
    private int temperature_status;
    private int light_status;
    private int moisture_status;
    private int salinity_status;
    private int nutrients_status;

    @Embedded
    @AttributeOverride(name = "last_fertilised_at", column = @Column(name = "fertilisation_last_fertilised_at"))
    @AttributeOverride(name = "fertilise_at", column = @Column(name = "fertilisation_fertilise_at"))
    @AttributeOverride(name = "was_repotted", column = @Column(name = "fertilisation_was_repotted"))
    private FertilisationEntity fertilisation;

    @Embedded
    @AttributeOverride(name = "light", column = @Column(name = "notifications_light"))
    @AttributeOverride(name = "temperature", column = @Column(name = "notifications_temperature"))
    @AttributeOverride(name = "water", column = @Column(name = "notifications_water"))
    @AttributeOverride(name = "nutrition", column = @Column(name = "notifications_nutrition"))
    private NotificationsEntity notifications;

    private int care_tips_count;
    private boolean has_remote_hub;
    private boolean has_remote_sensor;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "garden_id"))
    @AttributeOverride(name = "index", column = @Column(name = "garden_index"))
    private GardenEntity garden;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "sensor_id"))
    @AttributeOverride(name = "has_sensor", column = @Column(name = "sensor_has_sensor"))
    @AttributeOverride(name = "status", column = @Column(name = "sensor_status"))
    @AttributeOverride(name = "version", column = @Column(name = "sensor_version"))
    @AttributeOverride(name = "is_battery_low", column = @Column(name = "sensor_is_battery_low"))
    @AttributeOverride(name = "received_data_at", column = @Column(name = "sensor_received_data_at"))
    private SensorEntity sensor;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "hub_id"))
    @AttributeOverride(name = "hub_id", column = @Column(name = "hub_hub_id"))
    @AttributeOverride(name = "hub_name", column = @Column(name = "hub_hub_name"))
    @AttributeOverride(name = "version", column = @Column(name = "hub_version"))
    @AttributeOverride(name = "status", column = @Column(name = "hub_status"))
    @AttributeOverride(name = "received_data_at", column = @Column(name = "hub_received_data_at"))
    @AttributeOverride(name = "reached_hub_at", column = @Column(name = "hub_reached_hub_at"))
    private HubEntity hub;

    private boolean isSilent;
    private int noOfbadge;
    private boolean isBadge;
}
