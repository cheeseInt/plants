package ch.cheese.careui.repository;

import ch.cheese.careui.entity.PlantShortView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlantRepository extends JpaRepository<PlantShortView, Integer> {
    Optional<PlantShortView> findByNickname(String nickname);
}
