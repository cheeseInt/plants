package ch.cheese.plants.repository;

import ch.cheese.plants.entity.PlantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends JpaRepository<PlantEntity, Integer> {
    // zusätzliche Abfragen bei Bedarf:
    // Optional<PlantEntity> findByNickname(String nickname);
}
