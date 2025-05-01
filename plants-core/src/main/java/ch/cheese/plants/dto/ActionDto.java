package ch.cheese.plants.dto;

import lombok.Data;

@Data
public class ActionDto {
    private String action;
    private String title;
    private String description;
    private int priority;
}

