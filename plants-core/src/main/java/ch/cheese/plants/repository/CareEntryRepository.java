package ch.cheese.plants.repository;

import ch.cheese.plants.entity.PlantCareEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CareEntryRepository extends JpaRepository<PlantCareEntryEntity, Long> {
    Optional<PlantCareEntryEntity> findByPlant_IdAndCareTime(Long plantId, LocalDateTime dateUtc);
    List<PlantCareEntryEntity> findByPlant_IdAndCareTimeAfter(Long plantId, LocalDateTime dateUtc);
    List<PlantCareEntryEntity> findByPlant_Id(Long plantId);
    List<PlantCareEntryEntity> findByCareTimeAfter(LocalDateTime dateUtc);
}
