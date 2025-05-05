package ch.cheese.plants.repository;

import ch.cheese.plants.entity.CareEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CareEntryRepository extends JpaRepository<CareEntryEntity, Long> {
    Optional<CareEntryEntity> findByPlant_IdAndDateUtc(Long plantId, LocalDateTime dateUtc);
    List<CareEntryEntity> findByPlant_IdAndDateUtcAfter(Long plantId, LocalDateTime dateUtc);
    List<CareEntryEntity> findByPlant_Id(Long plantId);
    List<CareEntryEntity> findByDateUtcAfter(LocalDateTime dateUtc);
}
