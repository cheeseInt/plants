package ch.cheese.plants.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Plant {
    private int id;
    private String nickname;
    private String scientific_name;
    private String common_name;
    private int status;
    private Integer plant_id;
    private Integer airtable_id;
    private String genus;
    private Boolean is_shared;
    private Integer family_id;
    private Integer pot_size;
    private Integer drainage;
    private Double light_factor;
    private Integer soil_type_id;

    private String thumb_path;
    private String origin_path;
    private String plant_thumb_path;
    private String plant_origin_path;
    private String received_data_at;

    private boolean gathering_data;
    private boolean is_illegal;
    private boolean not_supported;

    private FertilisationDto fertilisation;
    private NotificationDto notifications;

    private boolean sensor_update_available;
    private String location;
    private boolean verification;
    private boolean is_productive_plant;
    private String dismissed_sensor_message_at;

    private GardenDto garden;
    private SensorDto sensor;
    private HubDto hub;

    private int temperature_status;
    private int light_status;
    private int moisture_status;
    private int salinity_status;
    private int nutrients_status;

    private int temperature_unit;

    private List<ActionDto> know_hows;
    private DeviceMenuDto device_menu;
    private List<String> missing;

    // weitere Felder möglich
}
