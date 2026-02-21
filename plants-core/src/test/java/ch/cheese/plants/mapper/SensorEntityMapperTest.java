package ch.cheese.plants.mapper;

import ch.cheese.plants.entity.SensorEntity;
import ch.cheese.plants.fyta.Sensor;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SensorEntityMapperTest {

    private final SensorEntityMapper mapper = new SensorEntityMapper();

    @Test
    void toEntity_whenNull_returnsNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    void toEntity_whenFullSensor_mapsAllFields() {
        Sensor sensor = new Sensor();
        sensor.setId("SENSOR-99");
        sensor.setHas_sensor(true);
        sensor.setStatus(2);
        sensor.setVersion("3.0");
        sensor.setReceived_data_at("2026-02-20 18:06:40");

        SensorEntity entity = mapper.toEntity(sensor);

        assertThat(entity.getId()).isEqualTo("SENSOR-99");
        assertThat(entity.getHas_sensor()).isTrue();
        assertThat(entity.getStatus()).isEqualTo(2);
        assertThat(entity.getVersion()).isEqualTo("3.0");
        assertThat(entity.getReceived_data_at()).isEqualTo(LocalDateTime.of(2026, 2, 20, 18, 6, 40));
    }

    @Test
    void toEntity_whenDateIsNull_mapsAsNull() {
        Sensor sensor = new Sensor();
        sensor.setReceived_data_at(null);

        SensorEntity entity = mapper.toEntity(sensor);

        assertThat(entity.getReceived_data_at()).isNull();
    }
}