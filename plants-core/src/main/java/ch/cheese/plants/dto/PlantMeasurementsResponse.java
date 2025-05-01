package ch.cheese.plants.dto;

import lombok.Data;

import java.util.List;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class PlantMeasurementsResponse {

    private List<MeasurementDto> measurements;

    private List<DliLightDTO> dli_light;

    private AbsoluteValues absolute_values;

    private Thresholds thresholds;

    private FertilisationDto fertilisation;

    private List<Thresholds> thresholds_list;
}
