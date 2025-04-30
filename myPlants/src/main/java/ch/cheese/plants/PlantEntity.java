package ch.cheese.plants;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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

    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeasurementEntity> measurements;
}
