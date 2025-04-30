package ch.cheese.plants.repository;

import ch.cheese.plants.entity.PlantCareEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantCareRepository extends JpaRepository<PlantCareEntity, Long> {
}
