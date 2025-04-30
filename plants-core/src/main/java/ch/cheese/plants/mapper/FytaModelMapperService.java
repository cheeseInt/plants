package ch.cheese.plants.mapper;

import ch.cheese.plants.dto.*;
import ch.cheese.plants.entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FytaModelMapperService {

    private final ObjectMapper objectMapper;

    public FertilisationEntity map(Fertilisation source) {
        if (source == null) return null;
        FertilisationEntity entity = new FertilisationEntity();
        entity.setLastFertilisedAt(source.getLast_fertilised_at());
        entity.setFertiliseAt(source.getFertilise_at());
        entity.setWasRepotted(source.getWas_repotted());
        return entity;
    }

    public NotificationsEntity map(Notifications source) {
        if (source == null) return null;
        NotificationsEntity entity = new NotificationsEntity();
        entity.setLight(source.getLight());
        entity.setTemperature(source.getTemperature());
        entity.setWater(source.getWater());
        entity.setNutrition(source.getNutrition());
        return entity;
    }

    public GardenEntity map(Garden source) {
        if (source == null) return null;
        GardenEntity entity = new GardenEntity();
        entity.setId(source.getId());
        entity.setName(source.getName());
        return entity;
    }

    public SensorEntity map(Sensor source) {
        if (source == null) return null;
        SensorEntity entity = new SensorEntity();
        entity.setId(source.getId());
        entity.setHasSensor(source.getHas_sensor());
        entity.setStatus(source.getStatus());
        entity.setVersion(source.getVersion());
        entity.setLightFactor(source.getLight_factor());
        entity.setProbeLengthId(source.getProbe_length_id());
        entity.setMoistureFactor(source.getMoisture_factor());
        entity.setBatteryLow(source.getIs_battery_low());
        entity.setReceivedDataAt(source.getReceived_data_at());
        entity.setCreatedAt(source.getCreated_at());
        entity.setHasSensorUpdate(source.getHas_sensor_update());
        return entity;
    }

    public HubEntity map(Hub source) {
        if (source == null) return null;
        HubEntity entity = new HubEntity();
        entity.setId(source.getId());
        entity.setHubId(source.getHub_id());
        entity.setHubName(source.getHub_name());
        entity.setVersion(source.getVersion());
        entity.setStatus(source.getStatus());
        entity.setReceivedDataAt(source.getReceived_data_at());
        entity.setReachedHubAt(source.getReached_hub_at());
        return entity;
    }

    public MeasurementsEntity map(Measurements source) {
        if (source == null) return null;
        MeasurementsEntity entity = new MeasurementsEntity();
        try {
            entity.setRawJson(objectMapper.writeValueAsString(source));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing Measurements", e);
        }
        return entity;
    }

    public DeviceMenuEntity map(DeviceMenu source) {
        if (source == null) return null;
        DeviceMenuEntity entity = new DeviceMenuEntity();
        try {
            entity.setRawJson(objectMapper.writeValueAsString(source));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing DeviceMenu", e);
        }
        return entity;
    }

    // --- Mapping all sub-objects into a single PlantEntity (convenience method) ---
    public void enrichPlantEntity(PlantEntity plantEntity, PlantEntry entry) {
        plantEntity.setFertilisation(map(entry.getFertilisation()));
        plantEntity.setNotifications(map(entry.getNotifications()));
        plantEntity.setSensor(map(entry.getSensor()));
        plantEntity.setHub(map(entry.getHub()));
        plantEntity.setGarden(map(entry.getGarden()));
        plantEntity.setMeasurements(map(entry.getMeasurements()));
        plantEntity.setDeviceMenu(map(entry.getDevice_menu()));
    }
}

