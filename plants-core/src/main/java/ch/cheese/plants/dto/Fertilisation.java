package ch.cheese.plants.dto;

import lombok.Data;

@Data
public class Fertilisation {
    private String last_fertilised_at;
    private String fertilise_at;
    private Boolean was_repotted;
}
