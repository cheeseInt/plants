package ch.cheese.plants.repository;

import ch.cheese.plants.entity.GardenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GardenRepository extends JpaRepository<GardenEntity, Integer> {
}
