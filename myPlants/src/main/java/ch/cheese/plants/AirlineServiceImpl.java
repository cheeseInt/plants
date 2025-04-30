package ch.cheese.plants;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AirlineServiceImpl implements AirlineService {

    List<AirlineDTO> paxAirlineDTOS = new ArrayList<>();
    List<AirlineDTO> pilotAirlineDTOS = new ArrayList<>();

    public AirlineServiceImpl() {
        pilotAirlineDTOS.add(new AirlineDTO(1, "LX", "Swiss International Air Lines", "SWISS", "Zürich Airport"));
        pilotAirlineDTOS.add(new AirlineDTO(2, "AA", "American Airlines", "AMERICAN", "Dallas/Fort Worth"));
        pilotAirlineDTOS.add(new AirlineDTO(3, "TG", "Thai Airways International", "THAI", "Suvarnabhumi Airport"));
        pilotAirlineDTOS.add(new AirlineDTO(4, "BA", "British Airways", "SPEEDBIRD", "Heathrow Airport"));

        for (AirlineDTO airline : pilotAirlineDTOS) {
            AirlineDTO airlinePax = new AirlineDTO();
            airlinePax.setId(airline.getId());
            airlinePax.setCodeIATA(airline.getCodeIATA());
            airlinePax.setName(airline.getName());
            airlinePax.setCallsign("***");
            airlinePax.setHub("***");
            paxAirlineDTOS.add(airlinePax);
        }
    }

    @Override
    public List<AirlineDTO> getAirlinesPax() {
        return paxAirlineDTOS;
    }

    @Override
    public List<AirlineDTO> getAirlinesPilot() {
        return pilotAirlineDTOS;
    }
}
