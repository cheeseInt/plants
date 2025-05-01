package ch.cheese.plants.repository;

import ch.cheese.plants.entity.PlantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantRepository extends JpaRepository<PlantEntity, Long> {

    Optional<PlantEntity> findById(Long id);

    // hier kannst du bei Bedarf weitere Abfragen definieren, z. B.:
    // List<PlantEntity> findByNicknameContainingIgnoreCase(String name);
}
