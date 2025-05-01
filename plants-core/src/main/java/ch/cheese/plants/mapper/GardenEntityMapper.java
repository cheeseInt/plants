package ch.cheese.plants.mapper;

import ch.cheese.plants.fyta.Garden;
import ch.cheese.plants.entity.GardenEntity;
import org.springframework.stereotype.Component;

@Component
public class GardenEntityMapper {
    public GardenEntity toEntity(Garden dto) {
        if (dto == null) return null;

        GardenEntity entity = new GardenEntity();
        entity.setId(dto.getId());
        entity.setIndex(dto.getIndex());
        return entity;
    }
}
