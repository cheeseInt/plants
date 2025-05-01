package ch.cheese.plants.repository;

import ch.cheese.plants.entity.MeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MeasurementRepository extends JpaRepository<MeasurementEntity, Long> {
    Optional<MeasurementEntity> findByPlantIdAndDateUtc(Long plantId, LocalDateTime dateUtc);
}
