package ch.cheese.plants.dto;

import lombok.Data;

@Data
public class NotificationDto {
    private boolean light;
    private boolean temperature;
    private boolean water;
    private boolean nutrition;}
