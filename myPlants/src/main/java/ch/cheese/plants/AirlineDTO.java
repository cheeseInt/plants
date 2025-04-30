package ch.cheese.plants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class AirlineDTO {

    private int id;
    private String codeIATA;
    private String name;
    private String callsign;
    private String hub;

    public AirlineDTO(int id, String codeIATA, String name, String callsign, String hub) {
        this.id = id;
        this.codeIATA = codeIATA;
        this.name = name;
        this.callsign = callsign;
        this.hub = hub;
    }
}
