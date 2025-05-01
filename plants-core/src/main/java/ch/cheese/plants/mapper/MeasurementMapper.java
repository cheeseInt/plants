package ch.cheese.plants.mapper;

import ch.cheese.plants.entity.MeasurementEntity;
import ch.cheese.plants.fyta.FytaMeasurementResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class MeasurementMapper {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MeasurementEntity toEntity(FytaMeasurementResponse dto, Long plantId) {
        MeasurementEntity e = new MeasurementEntity();
        e.setPlantId(plantId);
        e.setLight(dto.getLight());
        e.setTemperature(dto.getTemperature());
        e.setSoilMoisture(dto.getSoil_moisture());
        e.setSoilMoistureAnomaly(dto.isSoil_moisture_anomaly());
        e.setSoilFertility(dto.getSoil_fertility());
        e.setSoilFertilityAnomaly(dto.isSoil_fertility_anomaly());
        e.setThresholdsId(dto.getThresholds_id());
        if (dto.getDate_utc() != null) {
            e.setDateUtc(LocalDateTime.parse(dto.getDate_utc(), formatter));
        }
        log.info("MeasurementEntity ThresholdsId {} for plant with id {}: {}", dto.getThresholds_id(), plantId, e);
        log.info("MeasurementEntity Date {} for plant with id {}: {}", dto.getDate_utc(), plantId, e);
        return e;
    }
}
