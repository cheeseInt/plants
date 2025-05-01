package ch.cheese.plants.mapper;

import ch.cheese.plants.dto.*;
import ch.cheese.plants.entity.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FytaModelMapperService {

    private final ObjectMapper objectMapper;

    public Plant mapPlant(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode plantNode = root.get("plant");

            if (plantNode == null || plantNode.isNull()) {
                log.warn("🌱 Kein 'plant'-Knoten im JSON gefunden");
                return null;
            }

            return objectMapper.treeToValue(plantNode, Plant.class);

        } catch (Exception e) {
            log.error("❌ Fehler beim Mapping des Plant-JSON: {}", e.getMessage(), e);
            return null;
        }
    }

    public PlantEntity mapToEntity(Plant plant) {
        PlantEntity entity = new PlantEntity();
        entity.setId(plant.getId());
        entity.setNickname(plant.getNickname());
        entity.setScientificName(plant.getScientific_name());
        entity.setCommonName(plant.getCommon_name());
        entity.setStatus(plant.getStatus());
        entity.setPlantId(plant.getPlant_id());
        entity.setReceivedDataAt(plant.getReceived_data_at());
        entity.setThumbPath(plant.getThumb_path());
        entity.setOriginPath(plant.getOrigin_path());
        entity.setPlantThumbPath(plant.getPlant_thumb_path());
        entity.setPlantOriginPath(plant.getPlant_origin_path());
        entity.setLightStatus(plant.getLight_status());
        entity.setMoistureStatus(plant.getMoisture_status());
        entity.setNutrientsStatus(plant.getNutrients_status());
        entity.setSalinityStatus(plant.getSalinity_status());
        entity.setTemperatureStatus(plant.getTemperature_status());

        entity.setAirTableId(plant.getAirtable_id());
        entity.setGenus(plant.getGenus());
        entity.setIsShared(plant.getIs_shared());
        entity.setFamilyId(plant.getFamily_id());
        entity.setPotSize(plant.getPot_size());
        entity.setDrainage(plant.getDrainage());
        entity.setLightFactor(plant.getLight_factor());
        entity.setSoilTypeId(plant.getSoil_type_id());
        entity.setGatheringData(plant.isGathering_data());
        entity.setIllegal(plant.is_illegal());
        entity.setNotSupported(plant.isNot_supported());
        entity.setSensorUpdateAvailable(plant.isSensor_update_available());
        entity.setLocation(plant.getLocation());
        entity.setVerification(plant.isVerification());
        entity.setProductivePlant(plant.is_productive_plant());
        entity.setDismissedSensorMessageAt(plant.getDismissed_sensor_message_at());
        entity.setTemperatureUnit(plant.getTemperature_unit());

        FertilisationDto f = plant.getFertilisation();
        if (f != null) {
            FertilisationEmbedded fe = new FertilisationEmbedded();
            fe.setLastFertilisedAt(f.getLast_fertilised_at());
            fe.setFertiliseAt(f.getFertilise_at());
            fe.setWasRepotted(f.isWas_repotted());
            entity.setFertilisation(fe);
        }

        NotificationDto n = plant.getNotifications();
        if (n != null) {
            NotificationsEmbedded ne = new NotificationsEmbedded();
            ne.setLight(n.isLight());
            ne.setTemperature(n.isTemperature());
            ne.setWater(n.isWater());
            ne.setNutrition(n.isNutrition());
            entity.setNotifications(ne);
        }

        if (plant.getGarden() != null) {
            GardenDto g = plant.getGarden();
            GardenEntity ge = new GardenEntity();
            ge.setId(g.getId());
            ge.setName(g.getName());
            entity.setGarden(ge);
        }

        if (plant.getSensor() != null) {
            SensorDto s = plant.getSensor();
            SensorEntity se = new SensorEntity();
            se.setId(s.getId());
            se.setHasSensor(s.isHas_sensor());
            se.setStatus(s.getStatus());
            se.setVersion(s.getVersion());
            se.setLightFactor(s.getLight_factor());
            se.setProbeLengthId(s.getProbe_length_id());
            se.setMoistureFactor(s.getMoisture_factor());
            se.setBatteryLow(s.is_battery_low());
            se.setReceivedDataAt(s.getReceived_data_at());
            se.setCreatedAt(s.getCreated_at());
            se.setSensorUpdate(s.isHas_sensor_update());
            entity.setSensor(se);
        }

        if (plant.getHub() != null) {
            HubDto h = plant.getHub();
            HubEntity he = new HubEntity();
            he.setId(h.getId());
            he.setHubId(h.getHub_id());
            he.setHubName(h.getHub_name());
            he.setVersion(h.getVersion());
            he.setStatus(h.getStatus());
            he.setReceivedDataAt(h.getReceived_data_at());
            he.setReachedHubAt(h.getReached_hub_at());
            entity.setHub(he);
        }

        return entity;
    }
}
