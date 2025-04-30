package ch.cheese.plants;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface AirlineService {

    List<AirlineDTO> getAirlinesPax();

    List<AirlineDTO> getAirlinesPilot();
}
