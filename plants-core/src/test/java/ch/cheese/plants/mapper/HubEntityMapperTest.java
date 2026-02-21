package ch.cheese.plants.mapper;

import ch.cheese.plants.entity.HubEntity;
import ch.cheese.plants.fyta.Hub;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class HubEntityMapperTest {

    private final HubEntityMapper mapper = new HubEntityMapper();

    @Test
    void toEntity_whenNull_returnsNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    void toEntity_whenFullHub_mapsAllFields() {
        Hub hub = new Hub();
        hub.setId(42);
        hub.setHub_id("HUB-001");
        hub.setHub_name("My Hub");
        hub.setVersion("2.1");
        hub.setStatus(1);
        hub.setReceived_data_at("2025-10-22 11:44:54");
        hub.setReached_hub_at("2025-10-22 11:44:59");

        HubEntity entity = mapper.toEntity(hub);

        assertThat(entity.getId()).isEqualTo(42);
        assertThat(entity.getHub_id()).isEqualTo("HUB-001");
        assertThat(entity.getHub_name()).isEqualTo("My Hub");
        assertThat(entity.getVersion()).isEqualTo("2.1");
        assertThat(entity.getStatus()).isEqualTo(1);
        assertThat(entity.getReceived_data_at()).isEqualTo(LocalDateTime.of(2025, 10, 22, 11, 44, 54));
        assertThat(entity.getReached_hub_at()).isEqualTo(LocalDateTime.of(2025, 10, 22, 11, 44, 59));
    }

    @Test
    void toEntity_whenDatesAreNull_mapsAsNull() {
        Hub hub = new Hub();
        hub.setReceived_data_at(null);
        hub.setReached_hub_at(null);

        HubEntity entity = mapper.toEntity(hub);

        assertThat(entity.getReceived_data_at()).isNull();
        assertThat(entity.getReached_hub_at()).isNull();
    }
}