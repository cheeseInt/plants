package ch.cheese.plants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/api/v1/airlines")
public class AirlineControllerImpl implements AirlineController {

    private final AirlineService airlineService;

    public AirlineControllerImpl(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @Override
    public ResponseEntity<List<AirlineDTO>> getAirlineForPax() {
        return ResponseEntity.status(HttpStatus.OK).body(airlineService.getAirlinesPax());    }

    @Override
    public ResponseEntity<List<AirlineDTO>> getAirlineForPilot() {
        return ResponseEntity.status(HttpStatus.OK).body(airlineService.getAirlinesPilot());
    }
}
