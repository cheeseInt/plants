package ch.cheese.plants.mapper;

import ch.cheese.plants.entity.FertilisationEntity;
import ch.cheese.plants.fyta.Fertilisation;
import org.springframework.stereotype.Component;

@Component
public class FertilisationMapper {

    public FertilisationEntity toEntity(Fertilisation dto) {
        if (dto == null) return null;

        FertilisationEntity entity = new FertilisationEntity();
        entity.setLast_fertilised_at(dto.getLast_fertilised_at());
        entity.setFertilise_at(dto.getFertilise_at());
        entity.setWas_repotted(dto.isWas_repotted());
        return entity;
    }
}
