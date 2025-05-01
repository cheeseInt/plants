package ch.cheese.plants.mapper;

import ch.cheese.plants.entity.*;
import ch.cheese.plants.fyta.Plant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

    public PlantMapper(
            FertilisationEntityMapper fertilisationMapper,
            NotificationsEntityMapper notificationsMapper,
            GardenEntityMapper gardenMapper,
            SensorEntityMapper sensorMapper,
            HubEntityMapper hubMapper, ObjectMapper objectMapper
    ) {
        this.fertilisationMapper = fertilisationMapper;
        this.notificationsMapper = notificationsMapper;
        this.gardenMapper = gardenMapper;
        this.sensorMapper = sensorMapper;
        this.hubMapper = hubMapper;
        this.objectMapper = objectMapper;
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
        String timestamp = URLEncoder.encode(plant.getReceived_data_at(), StandardCharsets.UTF_8);
        String proxyUrl = "http://localhost:8081/api/proxy/plant-thumb/" + plant.getId();

        log.info("Timestamp: {}", timestamp);
        log.info("Proxy URL: {}", proxyUrl);

        entity.setThumb_proxy_url(proxyUrl);

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
    }}
