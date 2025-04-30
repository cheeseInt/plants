package ch.cheese.plants.dto;

import lombok.Data;
import java.util.List;

@Data
public class Plant {
    private int id;
    private String nickname;
    private String scientificName;
    private String commonName;
    private List<Measurements> measurements;
    private List<DliLight> dli_light;
    private AbsoluteValues absolute_values;
    private Thresholds thresholds;
    private Fertilisation fertilisation;
    private List<ThresholdList> thresholds_list;
}
