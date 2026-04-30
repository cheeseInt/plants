package ch.cheese.plants.mapper;

import ch.cheese.plants.entity.*;
import ch.cheese.plants.fyta.FytaPlantDetailResponse;
import ch.cheese.plants.fyta.Plant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class PlantMapper {

    @Value("${app.proxy-base-url}")
    private String proxyBaseUrl;

    private final FertilisationEntityMapper fertilisationMapper;
    private final NotificationsEntityMapper notificationsMapper;
    private final GardenEntityMapper gardenMapper;
    private final SensorEntityMapper sensorMapper;
    private final HubEntityMapper hubMapper;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter D_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

        entity.setProxy_thumb_url(proxyBaseUrl + "/proxy/thumb/" + plant.getId());


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
        entity.setReceived_data_at(plant.getReceived_data_at() != null ? LocalDateTime.parse(plant.getReceived_data_at(), DT_FORMATTER) : null);
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

        FytaPlantDetailResponse.Plant detailPlant = detail.getPlant();
        entity.setAirtable_id(detailPlant.getAirtable_id());
        if (entity.getGenus() == null && detailPlant.getGenus() != null) {
            entity.setGenus(detailPlant.getGenus());
        }
        if (detailPlant.getPot_size() != null) {
            entity.setPot_size(detailPlant.getPot_size());
        }
        if (detailPlant.getDrainage() != null) {
            entity.setDrainage(detailPlant.getDrainage());
        }
        if (detailPlant.getLight_factor() != null) {
            entity.setLight_factor(detailPlant.getLight_factor());
        }
        if (entity.getOwner() == null && detailPlant.getOwner() != null) {
            entity.setOwner(modelMapper.map(detailPlant.getOwner(), OwnerEntity.class));
        }
        if (detailPlant.getSoil_type_id() != null) {
            entity.setSoil_type_id(detailPlant.getSoil_type_id());
        }
        if (entity.getGathering_data() == null && detailPlant.getGathering_data() != null) {
            entity.setGathering_data(detailPlant.getGathering_data());
        }
        if (entity.getIs_illegal() == null && detailPlant.getIs_illegal() != null) {
            entity.setIs_illegal(detailPlant.getIs_illegal());
        }
        if (entity.getNot_supported() == null && detailPlant.getNot_supported() != null) {
            entity.setNot_supported(detailPlant.getNot_supported());
        }
        if (entity.getSensor_update_available() == null && detailPlant.getSensor_update_available() != null) {
            entity.setSensor_update_available(detailPlant.getSensor_update_available());
        }
        if (entity.getLocation() == null && detailPlant.getLocation() != null) {
            entity.setLocation(detailPlant.getLocation());
        }
        if (entity.getVerification() == null && detailPlant.getVerification() != null) {
            entity.setVerification(detailPlant.getVerification());
        }
        if (entity.getIs_productive_plant() == null && detailPlant.getIs_productive_plant() != null) {
            entity.setIs_productive_plant(detailPlant.getIs_productive_plant());
        }
        if (entity.getDismissed_sensor_message_at() == null && detailPlant.getDismissed_sensor_message_at() != null) {
            entity.setDismissed_sensor_message_at(LocalDate.parse(detailPlant.getDismissed_sensor_message_at(), D_FORMATTER));
        }
        if (entity.getMeasurements() == null && detailPlant.getMeasurements() != null) {
            entity.setMeasurements(modelMapper.map(detailPlant.getMeasurements(), MeasurementsEntity.class));
        }
        if (detailPlant.getTemperature_unit() != null) {
            entity.setTemperature_unit(detailPlant.getTemperature_unit());
        }
        if (entity.getDevice_menu() == null && detailPlant.getDevice_menu() != null) {
            entity.setDevice_menu(modelMapper.map(detailPlant.getDevice_menu(), Device_menuEntity.class));
        }
    }
}
