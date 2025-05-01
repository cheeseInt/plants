package ch.cheese.plants.fyta;

import ch.cheese.plants.dto.Device_menuDto;
import ch.cheese.plants.dto.MeasurementsDto;
import ch.cheese.plants.dto.OwnerDto;
import lombok.Data;
import java.util.List;

@Data
public class FytaPlantDetailResponse {
    private int airtable_id;
    private String genus;
    private int pot_size;
    private int drainage;
    private double light_factor;
    private OwnerDto owner;
    private int soil_type_id;
    private Boolean gathering_data;
    private Boolean is_illegal;
    private Boolean not_supported;
    private Boolean sensor_update_available;
    private String location;
    private Boolean verification;
    private Boolean is_productive_plant;
    private String dismissed_sensor_message_at;
    private List<String> missing;
    private MeasurementsDto measurements;
    private int temperature_unit;
    private List<String> know_hows;
    private Device_menuDto device_menu;

}
