package ch.cheese.plants.repository;

import ch.cheese.plants.entity.SensorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SensorRepository extends JpaRepository<SensorEntity, String> {
}
