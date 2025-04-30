package ch.cheese.plants.dto;

import lombok.Data;

@Data
public class PlantEntry {
    private Integer id;
    private Integer airtable_id;
    private String nickname;
    private Integer status;
    private String scientific_name;
    private String common_name;
    private String genus;
    private Integer plant_id;
    private Boolean is_shared;
    private Integer family_id;
    private Integer pot_size;
    private Integer drainage;
    private Double light_factor;

    private String thumb_path;
    private String origin_path;
    private String plant_thumb_path;
    private String plant_origin_path;
    private String received_data_at;

    private Boolean gathering_data;
    private Boolean is_illegal;
    private Boolean not_supported;
    private Boolean sensor_update_available;
    private Boolean verification;
    private Boolean is_productive_plant;
    private String dismissed_sensor_message_at;

    private Integer temperature_status;
    private Integer light_status;
    private Integer moisture_status;
    private Integer salinity_status;
    private Integer nutrients_status;

    private Fertilisation fertilisation;
    private Notifications notifications;
    private Garden garden;
    private Sensor sensor;
    private Hub hub;
    private Measurements measurements;
    private DeviceMenu device_menu;
}
