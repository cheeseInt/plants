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
        entity.setIs_shared(plant.getIs_shared());
        entity.setIndex(plant.getIndex());
        entity.setWifi_status(plant.getWifi_status());
        entity.setThumb_path(plant.getThumb_path());
        entity.setOrigin_path(plant.getOrigin_path());
        entity.setPlant_thumb_path(plant.getPlant_thumb_path());
        entity.setPlant_origin_path(plant.getPlant_origin_path());
        entity.setReceived_data_at(plant.getReceived_data_at());
        entity.setTemperature_optimal_hours(plant.getTemperature_optimal_hours());
        entity.setLight_optimal_hours(plant.getLight_optimal_hours());
        entity.setEligibility(plant.getEligibility());
        entity.setTemperature_status(plant.getTemperature_status());
        entity.setLight_status(plant.getLight_status());
        entity.setMoisture_status(plant.getMoisture_status());
        entity.setSalinity_status(plant.getSalinity_status());
        entity.setNutrients_status(plant.getNutrients_status());
        entity.setCare_tips_count(plant.getCare_tips_count());
        entity.setHas_remote_hub(plant.getHas_remote_hub());
        entity.setHas_remote_sensor(plant.getHas_remote_sensor());
        entity.setIsSilent(plant.getIsSilent());
        entity.setNoOfbadge(plant.getNoOfbadge());
        entity.setIsBadge(plant.getIsBadge());

        // Mapping der Embedded Objekte
        entity.setFertilisation(fertilisationMapper.toEntity(plant.getFertilisation()));
        entity.setNotifications(notificationsMapper.toEntity(plant.getNotifications()));
        entity.setGarden(gardenMapper.toEntity(plant.getGarden()));
        entity.setSensor(sensorMapper.toEntity(plant.getSensor()));
        entity.setHub(hubMapper.toEntity(plant.getHub()));

        return entity;
    }

    public void updateEntityWithDetails(PlantEntity entity, FytaPlantDetailResponse detail) {

        entity.setAirtable_id(detail.getPlant().getAirtable_id());
        if (entity.getGenus() == null && detail.getPlant().getGenus() != null) {
            entity.setGenus(detail.getPlant().getGenus());
        }
        entity.setPot_size(detail.getPlant().getPot_size());
        entity.setDrainage(detail.getPlant().getDrainage());
        entity.setLight_factor(detail.getPlant().getLight_factor());
        if (entity.getOwner() == null && detail.getPlant().getOwner() != null) {
            entity.setOwner(modelMapper.map(detail.getPlant().getOwner(), OwnerEntity.class));
        }
        entity.setSoil_type_id(detail.getPlant().getSoil_type_id());
        if (entity.getGathering_data() == null && detail.getPlant().getGathering_data() != null) {
            entity.setGathering_data(detail.getPlant().getGathering_data());
        }
        if (entity.getIs_illegal() == null && detail.getPlant().getIs_illegal() != null) {
            entity.setIs_illegal(detail.getPlant().getIs_illegal());
        }
        if (entity.getNot_supported() == null && detail.getPlant().getNot_supported() != null) {
            entity.setNot_supported(detail.getPlant().getNot_supported());
        }
        if (entity.getSensor_update_available() == null && detail.getPlant().getSensor_update_available() != null) {
            entity.setSensor_update_available(detail.getPlant().getSensor_update_available());
        }
        if (entity.getLocation() == null && detail.getPlant().getLocation() != null) {
            entity.setLocation(detail.getPlant().getLocation());
        }
        if (entity.getVerification() == null && detail.getPlant().getVerification() != null) {
            entity.setVerification(detail.getPlant().getVerification());
        }
        if (entity.getIs_productive_plant() == null && detail.getPlant().getIs_productive_plant() != null) {
            entity.setIs_productive_plant(detail.getPlant().getIs_productive_plant());
        }
        if (entity.getDismissed_sensor_message_at() == null && detail.getPlant().getDismissed_sensor_message_at() != null) {
            entity.setDismissed_sensor_message_at(detail.getPlant().getDismissed_sensor_message_at());
        }
        // TODO
//        if ((entity.getMissing() == null || entity.getMissing().isEmpty()) && detail.getPlant().getMissing() != null && !detail.getPlant().getMissing().isEmpty()) {
//            entity.setMissing(detail.getPlant().getMissing());
//        }
        if (entity.getMeasurements() == null && detail.getPlant().getMeasurements() != null) {
            entity.setMeasurements(modelMapper.map(detail.getPlant().getMeasurements(), MeasurementsEntity.class));
        }
        entity.setTemperature_unit(detail.getPlant().getTemperature_unit());
        // TODO
//        if ((entity.getKnow_hows() == null || entity.getKnow_hows().isEmpty()) && detail.getPlant().getKnow_hows() != null && !detail.getPlant().getKnow_hows().isEmpty()) {
//            entity.setKnow_hows(detail.getPlant().getKnow_hows());
//        }
        if (entity.getDevice_menu() == null && detail.getPlant().getDevice_menu() != null) {
            entity.setDevice_menu(modelMapper.map(detail.getPlant().getDevice_menu(), Device_menuEntity.class));
        }
    }
}
