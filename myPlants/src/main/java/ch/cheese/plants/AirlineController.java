package ch.cheese.plants;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@RequestMapping("default")
public interface AirlineController {

    @GetMapping("/pax")
    ResponseEntity<List<AirlineDTO>> getAirlineForPax();

    @GetMapping("/pilot")
    ResponseEntity<List<AirlineDTO>> getAirlineForPilot();
}
