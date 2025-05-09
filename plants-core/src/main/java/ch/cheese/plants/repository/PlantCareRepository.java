package ch.cheese.plants.repository;

import ch.cheese.plants.entity.PlantCareEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantCareRepository extends JpaRepository<PlantCareEntryEntity, Long> {
}
