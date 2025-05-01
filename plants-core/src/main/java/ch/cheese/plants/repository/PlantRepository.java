package ch.cheese.plants.repository;

import ch.cheese.plants.entity.PlantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends JpaRepository<PlantEntity, Long> {
    // du kannst hier bei Bedarf eigene Methoden hinzufügen, z. B.:
    // List<PlantEntity> findByNickname(String nickname);
}
