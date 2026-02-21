package ch.cheese.plants.mapper;

import ch.cheese.plants.fyta.Sensor;
import ch.cheese.plants.entity.SensorEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class SensorEntityMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SensorEntity toEntity(Sensor dto) {
        if (dto == null) return null;

        SensorEntity entity = new SensorEntity();
        entity.setId(dto.getId());
        entity.setHas_sensor(dto.isHas_sensor());
        entity.setStatus(dto.getStatus());
        entity.setVersion(dto.getVersion());
        entity.setIs_battery_low(dto.is_battery_low());
        entity.setReceived_data_at(dto.getReceived_data_at() != null ? LocalDateTime.parse(dto.getReceived_data_at(), FORMATTER) : null);
        return entity;
    }
}
