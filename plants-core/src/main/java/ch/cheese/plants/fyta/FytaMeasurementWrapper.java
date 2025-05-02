package ch.cheese.plants.fyta;

import lombok.Data;

import java.util.List;

@Data
public class FytaMeasurementWrapper {
    private List<FytaMeasurementResponse> measurements;
}
