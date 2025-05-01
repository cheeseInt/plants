package ch.cheese.plants.repository;


import ch.cheese.plants.entity.MeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MeasurementRepository extends JpaRepository<MeasurementEntity, String> {
}
