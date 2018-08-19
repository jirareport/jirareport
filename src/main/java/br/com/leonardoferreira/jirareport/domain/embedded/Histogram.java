package br.com.leonardoferreira.jirareport.domain.embedded;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Histogram {

    private Chart<Long, Long> chart;

    private Long median;

    private Long percentile75;

    private Long percentile90;

    @JsonIgnore
    public boolean getHasPercentile() {
        return median != null && percentile75 != null && percentile90 != null;
    }

}
