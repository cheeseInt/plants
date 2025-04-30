package ch.cheese.plants;

import lombok.Data;

@Data
public class ThresholdList {
    private int ph_min;
    private int ph_max;
    private int temperature_min_good;
    private int temperature_max_good;
    private int temperature_min_acceptable;
    private int temperature_max_acceptable;
    private double light_min_good;
    private double light_max_good;
    private double light_min_acceptable;
    private double light_max_acceptable;
    private double dli_light_min_good;
    private double dli_light_max_good;
    private double dli_light_min_acceptable;
    private double dli_light_max_acceptable;
    private int moisture_min_good;
    private int moisture_max_good;
    private int moisture_min_acceptable;
    private int moisture_max_acceptable;
    private double salinity_min_good;
    private double salinity_max_good;
    private double salinity_min_acceptable;
    private double salinity_max_acceptable;
    private int watering_cycles_until_fertilisation;
    private int days_until_fertilisation;
    private int id;
    private String thresholds_type;
    private String photoperiod;
}
