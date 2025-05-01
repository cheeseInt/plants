package ch.cheese.plants.dto;

import lombok.Data;

@Data
public class FertilisationDto {
    private String last_fertilised_at;
    private String fertilise_at;
    private boolean was_repotted;}
