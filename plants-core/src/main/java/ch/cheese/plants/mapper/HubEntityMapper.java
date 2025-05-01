package ch.cheese.plants.mapper;

import ch.cheese.plants.fyta.Hub;
import ch.cheese.plants.entity.HubEntity;
import org.springframework.stereotype.Component;

@Component
public class HubEntityMapper {
    public HubEntity toEntity(Hub dto) {
        if (dto == null) return null;

        HubEntity entity = new HubEntity();
        entity.setId(dto.getId());
        entity.setHub_id(dto.getHub_id());
        entity.setHub_name(dto.getHub_name());
        entity.setVersion(dto.getVersion());
        entity.setStatus(dto.getStatus());
        entity.setReceived_data_at(dto.getReceived_data_at());
        entity.setReached_hub_at(dto.getReached_hub_at());
        return entity;
    }
}
