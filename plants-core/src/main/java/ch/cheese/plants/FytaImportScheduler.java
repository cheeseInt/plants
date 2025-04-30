package ch.cheese.plants;

import ch.cheese.plants.controller.PlantController;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class FytaImportScheduler {

    private final PlantController plantController;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void importOnceAtStartup() {
        log.info("📦 [Startup] FYTA-Import startet um {}", now());
        try {
            plantController.importMyPlant("month");
            log.info("✅ [Startup] FYTA-Import erfolgreich um {}", now());
        } catch (Exception e) {
            log.error("❌ [Startup] FYTA-Import fehlgeschlagen um {}", now(), e);
        }
    }

    @Scheduled(fixedRate = 21600000) // alle 6h
    public void importEverySixHours() {
        log.info("🔄 [Timer] FYTA-Import beginnt um {}", now());
        try {
            plantController.importMyPlant("day");
            log.info("✅ [Timer] FYTA-Import erfolgreich um {}", now());
        } catch (Exception e) {
            log.error("❌ [Timer] FYTA-Import fehlgeschlagen um {}", now(), e);
        }
    }

    private String now() {
        return LocalDateTime.now().format(formatter);
    }
}
