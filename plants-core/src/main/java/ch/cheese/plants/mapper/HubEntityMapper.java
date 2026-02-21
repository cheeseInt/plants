package ch.cheese.plants.mapper;

import ch.cheese.plants.fyta.Hub;
import ch.cheese.plants.entity.HubEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class HubEntityMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HubEntity toEntity(Hub dto) {
        if (dto == null) return null;

        HubEntity entity = new HubEntity();
        entity.setId(dto.getId());
        entity.setHub_id(dto.getHub_id());
        entity.setHub_name(dto.getHub_name());
        entity.setVersion(dto.getVersion());
        entity.setStatus(dto.getStatus());
        entity.setReceived_data_at(dto.getReceived_data_at() != null ? LocalDateTime.parse(dto.getReceived_data_at(), FORMATTER) : null);
        entity.setReached_hub_at(dto.getReached_hub_at() != null ? LocalDateTime.parse(dto.getReached_hub_at(), FORMATTER) : null);
        return entity;
    }
}
