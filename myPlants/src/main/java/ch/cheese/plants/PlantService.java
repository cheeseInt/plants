package ch.cheese.plants;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlantService {

    @PersistenceContext
    private EntityManager em;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public void savePlants(Map<PlantEntry, Plant> plantData) {
        for (Map.Entry<PlantEntry, Plant> entry : plantData.entrySet()) {
            PlantEntry plantEntry = entry.getKey();
            Plant plantMeasurement = entry.getValue();

            PlantEntity plantEntity = em.find(PlantEntity.class, plantEntry.getId());
            if (plantEntity == null) {
                plantEntity = new PlantEntity();
                plantEntity.setId(plantEntry.getId());
            }

            updatePlantEntityFromEntry(plantEntity, plantEntry);
            handleMeasurementsWithExistCheck(plantEntity, plantMeasurement);

            em.persist(plantEntity);
        }
    }

    private void updatePlantEntityFromEntry(PlantEntity plantEntity, PlantEntry plantEntry) {
        plantEntity.setNickname(plantEntry.getNickname());
        plantEntity.setScientificName(plantEntry.getScientific_name());
        plantEntity.setCommonName(plantEntry.getCommon_name());
        plantEntity.setStatus(plantEntry.getStatus());
        plantEntity.setPlantId(plantEntry.getPlant_id());
        plantEntity.setFamilyId(plantEntry.getFamily_id());
        plantEntity.setWifiStatus(plantEntry.getWifi_status());
        plantEntity.setThumbPath(plantEntry.getThumb_path());
        plantEntity.setOriginPath(plantEntry.getOrigin_path());
        plantEntity.setPlantThumbPath(plantEntry.getPlant_thumb_path());
        plantEntity.setPlantOriginPath(plantEntry.getPlant_origin_path());
        plantEntity.setReceivedDataAt(plantEntry.getReceived_data_at());
        plantEntity.setTemperatureOptimalHours(plantEntry.getTemperature_optimal_hours());
        plantEntity.setLightOptimalHours(plantEntry.getLight_optimal_hours());
        plantEntity.setEligibility(plantEntry.isEligibility());
        plantEntity.setTemperatureStatus(plantEntry.getTemperature_status());
        plantEntity.setLightStatus(plantEntry.getLight_status());
        plantEntity.setMoistureStatus(plantEntry.getMoisture_status());
        plantEntity.setSalinityStatus(plantEntry.getSalinity_status());
        plantEntity.setNutrientsStatus(plantEntry.getNutrients_status());
        plantEntity.setCareTipsCount(plantEntry.getCare_tips_count());
        plantEntity.setHasRemoteHub(plantEntry.isHas_remote_hub());
        plantEntity.setHasRemoteSensor(plantEntry.isHas_remote_sensor());
        plantEntity.setIsSilent(plantEntry.getIsSilent());
        plantEntity.setIsDoingGreat(plantEntry.getIsDoingGreat());
    }

    private void handleMeasurementsWithExistCheck(PlantEntity plantEntity, Plant plantMeasurement) {
        if (plantMeasurement.getMeasurements() == null) return;

        List<MeasurementEntity> newMeasurements = new ArrayList<>();

        for (var m : plantMeasurement.getMeasurements()) {
            LocalDateTime dateUtc = LocalDateTime.parse(m.getDate_utc(), DATE_TIME_FORMATTER);

            boolean exists = em.createQuery(
                            "SELECT COUNT(m) FROM MeasurementEntity m WHERE m.plant.id = :plantId AND m.dateUtc = :dateUtc", Long.class)
                    .setParameter("plantId", plantEntity.getId())
                    .setParameter("dateUtc", dateUtc)
                    .getSingleResult() > 0;

            if (exists) continue;

            MeasurementEntity me = new MeasurementEntity();
            me.setPlant(plantEntity);
            me.setLight(m.getLight());
            me.setTemperature(m.getTemperature());
            me.setSoilMoisture(m.getSoil_moisture());
            me.setSoilMoistureAnomaly(m.isSoil_moisture_anomaly());
            me.setSoilFertility(m.getSoil_fertility());
            me.setSoilFertilityAnomaly(m.isSoil_fertility_anomaly());
            me.setThresholdsId(m.getThresholds_id());
            me.setDateUtc(dateUtc);

            newMeasurements.add(me);
        }

        if (plantEntity.getMeasurements() == null) {
            plantEntity.setMeasurements(new ArrayList<>());
        }

        plantEntity.getMeasurements().addAll(newMeasurements);
    }
}
