package br.com.jiratorio.domain.entity.embedded;

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

}
