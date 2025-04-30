package ch.cheese.plants.service;

import ch.cheese.plants.dto.CareRequest;
import ch.cheese.plants.entity.PlantCareEntity;
import ch.cheese.plants.entity.PlantEntity;
import ch.cheese.plants.repository.PlantCareRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlantCareService {

    private final PlantCareRepository careRepository;

    @PersistenceContext
    private EntityManager em;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Transactional
    public boolean addCareEntry(Integer plantId, CareRequest request) {
        PlantEntity plant = em.find(PlantEntity.class, plantId);
        if (plant == null) {
            throw new IllegalArgumentException("Plant with ID " + plantId + " not found");
        }

        // Datum parsen
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(request.getDate(), DATE_FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: expected dd.MM.yyyy");
        }

        LocalDateTime dateUtc = parsedDate.atStartOfDay(); // 00:00 Uhr

        PlantCareEntity existingCare = em.createQuery(
                        "SELECT c FROM PlantCareEntity c WHERE c.plant.id = :plantId AND c.dateUtc = :dateUtc",
                        PlantCareEntity.class
                )
                .setParameter("plantId", plantId)
                .setParameter("dateUtc", dateUtc)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (existingCare != null) {
            log.info("Updating existing care entry for plant {} at {}", plantId, dateUtc);
            existingCare.setWaterInLiters(request.getWaterInLiters());
            existingCare.setFertilizerInMl(request.getFertilizerInMl());
            em.merge(existingCare);
            return false; // Es war ein Update
        } else {
            log.info("Creating new care entry for plant {} at {}", plantId, dateUtc);
            PlantCareEntity care = new PlantCareEntity();
            care.setPlant(plant);
            care.setWaterInLiters(request.getWaterInLiters());
            care.setFertilizerInMl(request.getFertilizerInMl());
            care.setDateUtc(dateUtc);
            careRepository.save(care);
            return true; // Neuer Eintrag
        }
    }
}
