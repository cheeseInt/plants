package ch.cheese.plants.fyta;

import lombok.Data;

import java.util.List;

@Data
public class FytaPlantDetailResponse {
    private Plant plant;

    @Data
    public static class Plant {
        private int id;
        private int airtable_id;
        private String nickname;
        private int status;
        private String scientific_name;
        private String genus;
        private Integer plant_id;
        private Boolean is_shared;
        private Integer family_id;
        private Integer pot_size;
        private Integer drainage;
        private Double light_factor;
        private Owner owner;
        private List<Object> peers;
        private Integer soil_type_id;
        private String thumb_path;
        private String origin_path;
        private String plant_thumb_path;
        private String plant_origin_path;
        private String received_data_at;
        private Boolean gathering_data;
        private Boolean is_illegal;
        private Boolean not_supported;
        private Fertilisation fertilisation;
        private Notifications notifications;
        private Boolean sensor_update_available;
        private String location;
        private Boolean verification;
        private Boolean is_productive_plant;
        private String dismissed_sensor_message_at;
        private Garden garden;
        private Sensor sensor;
        private Hub hub;
        private List<Object> missing;
        private Measurements measurements;
        private Integer temperature_unit;
        private List<Object> know_hows;
        private DeviceMenu device_menu;
        private List<SensorInfo> sensors;    }

    @Data
    public static class Owner {
        // leer oder individuell anpassbar bei Bedarf
    }

    @Data
    public static class SensorInfo {
        private String id;
        private Integer status;
        private String version;
        private Integer sensor_type_id;
        private Double light_factor;
        private Integer probe_length_id;
        private Double moisture_factor;
        private Integer battery_level;
        private String last_data_received_at;
        private String created_at;
    }

    @Data
    public static class Fertilisation {
        private String last_fertilised_at;
        private String fertilise_at;
        private Boolean was_repotted;
    }

    @Data
    public static class Notifications {
        private Boolean light;
        private Boolean temperature;
        private Boolean water;
        private Boolean nutrition;
    }

    @Data
    public static class Garden {
        private Integer id;
        private String name;
    }

    @Data
    public static class Sensor {
        private String id;
        private Boolean has_sensor;
        private Integer status;
        private String version;
        private Double light_factor;
        private Integer probe_length_id;
        private Double moisture_factor;
        private Boolean is_battery_low;
        private String received_data_at;
        private String created_at;
        private Boolean has_sensor_update;
    }

    @Data
    public static class Hub {
        private Integer id;
        private String hub_id;
        private String hub_name;
        private Integer status;
        private String received_data_at;
        private String reached_hub_at;
    }

    @Data
    public static class Measurements {
        private Measurement ph;
        private Measurement nutrients;
        private Measurement temperature;
        private LightMeasurement light;
        private Measurement moisture;
        private Measurement salinity;
        private String battery;
    }

    @Data
    public static class Measurement {
        private String type;
        private Integer status;
        private Values values;
        private String unit;
        private AbsoluteValues absolute_values;
    }

    @Data
    public static class LightMeasurement extends Measurement {
        private DliValues dli_values;
        private String dli_unit;
    }

    @Data
    public static class Values {
        private String min;
        private String max;
        private String current;

        private String min_good;
        private String max_good;
        private String min_acceptable;
        private String max_acceptable;
        private String currentFormatted;
        private Integer optimal_hours;
    }

    @Data
    public static class AbsoluteValues {
        private String min;
        private String max;
        private String minText;
        private String maxText;
    }

    @Data
    public static class DliValues {
        private String min_good;
        private String max_good;
        private String min_acceptable;
        private String max_acceptable;
    }

    @Data
    public static class DeviceMenu {
        private List<Action> actions;
        private List<String> buttons;
        private Boolean live_mode_enabled;
        private Boolean diagnose_enabled;
    }

    @Data
    public static class Action {
        private String action;
        private String title;
        private String description;
        private Integer priority;
    }
}
