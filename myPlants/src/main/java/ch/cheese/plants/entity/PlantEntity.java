package ch.cheese.plants.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "plants")
@Data
public class PlantEntity {

    @Id
    private Integer id; // Plant-ID aus Fyta API

    private String nickname;
    private String scientificName;
    private String commonName;

    private Integer status;
    private Integer plantId;
    private Integer familyId;
    private Integer wifiStatus;

    private String thumbPath;
    private String originPath;
    private String plantThumbPath;
    private String plantOriginPath;

    private String receivedDataAt;
    private Integer temperatureOptimalHours;
    private Integer lightOptimalHours;

    private Boolean eligibility;
    private Integer temperatureStatus;
    private Integer lightStatus;
    private Integer moistureStatus;
    private Integer salinityStatus;
    private Integer nutrientsStatus;

    private Integer careTipsCount;
    private Boolean hasRemoteHub;
    private Boolean hasRemoteSensor;

    private Boolean isSilent;
    private Boolean isDoingGreat;

    private Integer airtableId;
    private String genus;
    private Integer potSize;
    private Integer drainage;
    private Integer soilTypeId;
    private Integer lightFactor;
    private Boolean gatheringData;
    private Boolean isIllegal;
    private Boolean notSupported;
    private Boolean sensorUpdateAvailable;
    private Boolean verification;
    private Boolean isProductivePlant;
    private String dismissedSensorMessageAt;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private FertilisationEntity fertilisation;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private NotificationsEntity notifications;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private GardenEntity garden;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private SensorEntity sensor;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private HubEntity hub;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private MeasurementsEntity measurements;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private DeviceMenuEntity deviceMenu;
}
