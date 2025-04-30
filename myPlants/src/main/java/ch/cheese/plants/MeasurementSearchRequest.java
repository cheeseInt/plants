package ch.cheese.plants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementSearchRequest {
    private Search search;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Search {
        private String timeline;
    }
}
