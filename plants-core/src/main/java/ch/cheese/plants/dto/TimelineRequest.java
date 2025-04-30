package ch.cheese.plants.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimelineRequest {

    private String timeline; // „day“, „week“ oder „month“
}
