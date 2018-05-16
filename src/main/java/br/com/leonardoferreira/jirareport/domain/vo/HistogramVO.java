package br.com.leonardoferreira.jirareport.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistogramVO {

    private Long median;

    private Long percentile75;

    private Long percentile90;

}
