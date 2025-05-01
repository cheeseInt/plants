package ch.cheese.plants.repository;

import ch.cheese.plants.entity.HubEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HubRepository extends JpaRepository<HubEntity, Integer> {
}
