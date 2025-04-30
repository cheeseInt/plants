package ch.cheese.plants;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantCareRepository extends JpaRepository<PlantCareEntity, Long> {
}
