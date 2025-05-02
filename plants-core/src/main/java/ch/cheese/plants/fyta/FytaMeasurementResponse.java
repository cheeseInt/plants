package ch.cheese.plants.fyta;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class FytaMeasurementResponse {
    private int light;
    private int temperature;
    private int soil_moisture;
    private boolean soil_moisture_anomaly;
    private int soil_fertility;
    private boolean soil_fertility_anomaly;
    private int thresholds_id;
    @JsonProperty("date_utc")
    private String date_utc;
}
