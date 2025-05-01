package ch.cheese.plants.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plants")
@Data
@NoArgsConstructor
public class PlantEntity {

    @Id
    private Integer id;

    private String nickname;
    private String scientificName;
    private String commonName;
    private Integer status;
    private Integer plantId;

    private String receivedDataAt;
    private String thumbPath;
    private String originPath;
    private String plantThumbPath;
    private String plantOriginPath;

    private Integer lightStatus;
    private Integer moistureStatus;
    private Integer nutrientsStatus;
    private Integer salinityStatus;
    private Integer temperatureStatus;

    private Integer airTableId;
    private String genus;
    private Boolean isShared;
    private Integer familyId;
    private Integer potSize;
    private Integer drainage;
    private Double lightFactor;
    private Integer soilTypeId;

    private boolean gatheringData;
    private boolean isIllegal;
    private boolean notSupported;
    private boolean sensorUpdateAvailable;
    private String location;
    private boolean verification;
    private boolean isProductivePlant;
    private String dismissedSensorMessageAt;
    private Integer temperatureUnit;

    @Embedded
    private FertilisationEmbedded fertilisation;

    @Embedded
    private NotificationsEmbedded notifications;

    @OneToOne
    @JoinColumn(name = "garden_id")
    private GardenEntity garden;

    @OneToOne
    @JoinColumn(name = "sensor_id")
    private SensorEntity sensor;

    @OneToOne
    @JoinColumn(name = "hub_id")
    private HubEntity hub;

}
