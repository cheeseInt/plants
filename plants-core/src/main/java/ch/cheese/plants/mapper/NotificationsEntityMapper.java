package ch.cheese.plants.mapper;

import ch.cheese.plants.fyta.Notifications;
import ch.cheese.plants.entity.NotificationsEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationsEntityMapper {
    public NotificationsEntity toEntity(Notifications dto) {
        if (dto == null) return null;

        NotificationsEntity entity = new NotificationsEntity();
        entity.setLight(dto.isLight());
        entity.setTemperature(dto.isTemperature());
        entity.setWater(dto.isWater());
        entity.setNutrition(dto.isNutrition());
        return entity;
    }
}
