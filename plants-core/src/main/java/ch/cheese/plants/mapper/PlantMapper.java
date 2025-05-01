package ch.cheese.plants.mapper;

import ch.cheese.plants.entity.*;
import ch.cheese.plants.fyta.FytaPlantDetailResponse;
import ch.cheese.plants.fyta.Plant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class PlantMapper {

    private final FertilisationEntityMapper fertilisationMapper;
    private final NotificationsEntityMapper notificationsMapper;
    private final GardenEntityMapper gardenMapper;
    private final SensorEntityMapper sensorMapper;
    private final HubEntityMapper hubMapper;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;


    public PlantMapper(
            FertilisationEntityMapper fertilisationMapper,
            NotificationsEntityMapper notificationsMapper,
            GardenEntityMapper gardenMapper,
            SensorEntityMapper sensorMapper,
            HubEntityMapper hubMapper,
            ObjectMapper objectMapper,
            ModelMapper modelMapper

    ) {
        this.fertilisationMapper = fertilisationMapper;
        this.notificationsMapper = notificationsMapper;
        this.gardenMapper = gardenMapper;
        this.sensorMapper = sensorMapper;
        this.hubMapper = hubMapper;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    public PlantEntity toEntity(Plant plant) {
        PlantEntity entity = new PlantEntity();
        entity.setId((long) plant.getId());
        entity.setNickname(plant.getNickname());

        try {
            entity.setPeersJson(objectMapper.writeValueAsString(plant.getPeers()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Fehler beim Serialisieren von peers", e);
        }

        entity.setProxy_thumb_url("http://localhost:8081/proxy/thumb/" +plant.getId());


        entity.setScientific_name(plant.getScientific_name());
        entity.setCommon_name(plant.getCommon_name());
        entity.setStatus(plant.getStatus());
        entity.setPlant_id(plant.getPlant_id());
        entity.setFamily_id(plant.getFamily_id());
        entity.set_shared(plant.is_shared());
        entity.setIndex(plant.getIndex());
        entity.setWifi_status(plant.getWifi_status());
        entity.setThumb_path(plant.getThumb_path());
        entity.setOrigin_path(plant.getOrigin_path());
        entity.setPlant_thumb_path(plant.getPlant_thumb_path());
        entity.setPlant_origin_path(plant.getPlant_origin_path());
        entity.setReceived_data_at(plant.getReceived_data_at());
        entity.setTemperature_optimal_hours(plant.getTemperature_optimal_hours());
        entity.setLight_optimal_hours(plant.getLight_optimal_hours());
        entity.setEligibility(plant.isEligibility());
        entity.setTemperature_status(plant.getTemperature_status());
        entity.setLight_status(plant.getLight_status());
        entity.setMoisture_status(plant.getMoisture_status());
        entity.setSalinity_status(plant.getSalinity_status());
        entity.setNutrients_status(plant.getNutrients_status());
        entity.setCare_tips_count(plant.getCare_tips_count());
        entity.setHas_remote_hub(plant.isHas_remote_hub());
        entity.setHas_remote_sensor(plant.isHas_remote_sensor());
        entity.setSilent(plant.isSilent());
        entity.setNoOfbadge(plant.getNoOfbadge());
        entity.setBadge(plant.isBadge());

        // Mapping der Embedded Objekte
        entity.setFertilisation(fertilisationMapper.toEntity(plant.getFertilisation()));
        entity.setNotifications(notificationsMapper.toEntity(plant.getNotifications()));
        entity.setGarden(gardenMapper.toEntity(plant.getGarden()));
        entity.setSensor(sensorMapper.toEntity(plant.getSensor()));
        entity.setHub(hubMapper.toEntity(plant.getHub()));

        return entity;
    }

    public void updateEntityWithDetails(PlantEntity entity, FytaPlantDetailResponse detail) {

        entity.setAirtable_id(detail.getAirtable_id());
        if (entity.getGenus() == null) {
            entity.setGenus(detail.getGenus());
        }
        entity.setPot_size(detail.getPot_size());
        entity.setDrainage(detail.getDrainage());
        entity.setLight_factor(detail.getLight_factor());
        if (entity.getOwner() == null) {
            entity.setOwner(modelMapper.map(detail.getOwner(), OwnerEntity.class));
        }
        entity.setSoil_type_id(detail.getSoil_type_id());
        entity.setGathering_data(detail.isGathering_data());
        entity.set_illegal(detail.is_illegal());
        entity.setNot_supported(detail.isNot_supported());
        entity.setSensor_update_available(detail.isSensor_update_available());
        if (entity.getLocation() == null) {
            entity.setLocation(detail.getLocation());
        }
        entity.setVerification(detail.isVerification());
        entity.set_productive_plant(detail.is_productive_plant());
        if (entity.getDismissed_sensor_message_at() == null) {
            entity.setDismissed_sensor_message_at(detail.getDismissed_sensor_message_at());
        }
        if (entity.getMissing() == null || entity.getMissing().isEmpty()) {
            entity.setMissing(detail.getMissing());
        }
        if (entity.getMeasurements() == null) {
            entity.setMeasurements(modelMapper.map(detail.getMeasurements(), MeasurementsEntity.class));
        }
        entity.setTemperature_unit(detail.getTemperature_unit());
        if (entity.getKnow_hows() == null || entity.getKnow_hows().isEmpty()) {
            entity.setKnow_hows(detail.getKnow_hows());
        }
        if (entity.getDevice_menu() == null) {
            entity.setDevice_menu(modelMapper.map(detail.getDevice_menu(), Device_menuEntity.class));
        }
    }

}
