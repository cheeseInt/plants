package ch.cheese.plants.repository;

import ch.cheese.plants.entity.BatteryLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatteryLogRepository extends JpaRepository<BatteryLogEntity, Long> {
}
