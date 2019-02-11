package br.com.jiratorio.domain.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Percentile {

    private Double average;

    private Long median;

    private Long percentile75;

    private Long percentile90;

}
