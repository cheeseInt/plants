package ch.cheese.plants.fyta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Plant {
    @JsonProperty("id")
    private int id;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("scientific_name")
    private String scientific_name;
    @JsonProperty("common_name")
    private String common_name;
    @JsonProperty("status")
    private int status;
    @JsonProperty("plant_id")
    private int plant_id;
    @JsonProperty("family_id")
    private String family_id;
    @JsonProperty("is_shared")
    private boolean is_shared;
    @JsonProperty("owner")
    private Owner owner;
    @JsonProperty("peers")
    private List<Object> peers;
    @JsonProperty("index")
    private String index;
    @JsonProperty("wifi_status")
    private int wifi_status;
    @JsonProperty("thumb_path")
    private String thumb_path;
    @JsonProperty("origin_path")
    private String origin_path;
    @JsonProperty("plant_thumb_path")
    private String plant_thumb_path;
    @JsonProperty("plant_origin_path")
    private String plant_origin_path;
    @JsonProperty("received_data_at")
    private String received_data_at;
    @JsonProperty("temperature_optimal_hours")
    private int temperature_optimal_hours;
    @JsonProperty("light_optimal_hours")
    private int light_optimal_hours;
    @JsonProperty("eligibility")
    private boolean eligibility;
    @JsonProperty("temperature_status")
    private int temperature_status;
    @JsonProperty("light_status")
    private int light_status;
    @JsonProperty("moisture_status")
    private int moisture_status;
    @JsonProperty("salinity_status")
    private int salinity_status;
    @JsonProperty("nutrients_status")
    private int nutrients_status;
    @JsonProperty("fertilisation")
    private Fertilisation fertilisation;
    @JsonProperty("notifications")
    private Notifications notifications;
    @JsonProperty("care_tips_count")
    private int care_tips_count;
    @JsonProperty("has_remote_hub")
    private boolean has_remote_hub;
    @JsonProperty("has_remote_sensor")
    private boolean has_remote_sensor;
    @JsonProperty("garden")
    private Garden garden;
    @JsonProperty("sensor")
    private Sensor sensor;
    @JsonProperty("hub")
    private Hub hub;
    @JsonProperty("isSilent")
    private boolean isSilent;
    @JsonProperty("noOfbadge")
    private int noOfbadge;
    @JsonProperty("isBadge")
    private boolean isBadge;
}
