package ch.cheese.plants.scheduler;

import ch.cheese.plants.config.Timeline;
import ch.cheese.plants.service.PlantImportService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlantImportScheduler {

    private final PlantImportService plantImportService;

    // 🟢 Beim Start der App
    @PostConstruct
    public void importOnceOnStartup() {
        log.info("Running initial plant import with Timeline.MONTH");
        plantImportService.importPlantsFromFyta(Timeline.MONTH);
    }

    // 🔁 Alle 6 Stunden
    @Scheduled(cron = "0 0 */6 * * *") // jede sechste volle Stunde (z. B. 00:00, 06:00, 12:00, 18:00)
    public void importRegularly() {
        log.info("Running scheduled plant import with Timeline.DAY");
        plantImportService.importPlantsFromFyta(Timeline.DAY);
    }
}
