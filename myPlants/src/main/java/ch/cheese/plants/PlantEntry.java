package ch.cheese.plants;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlantEntry {
    private int id;
    private String nickname;
    private String scientific_name;
    private String common_name;
    private int status;
    private int plant_id;
    private Integer family_id;
    private boolean is_shared;
    private Owner owner;
    private List<Object> peers;
    private Integer index;
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
    private Fertilisation fertilisation;
    private Notifications notifications;
    private int care_tips_count;
    private boolean has_remote_hub;
    private boolean has_remote_sensor;
    private Garden garden;
    private Sensor sensor;
    private Hub hub;
    private Boolean isSilent;
    private Boolean isDoingGreat;
}
