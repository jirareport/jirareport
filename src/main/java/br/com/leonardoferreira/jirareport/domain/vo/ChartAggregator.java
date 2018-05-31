package br.com.leonardoferreira.jirareport.domain.vo;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.embedded.Chart;
import br.com.leonardoferreira.jirareport.domain.embedded.ColumnTimeAvg;
import br.com.leonardoferreira.jirareport.domain.embedded.Histogram;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lferreira
 * @since 5/11/18 12:33 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartAggregator {

    private Histogram histogram;

    private Chart<String, Long> estimated;

    private Chart<String, Double> leadTimeBySystem;

    private Chart<String, Long> tasksBySystem;

    private Chart<String, Double> leadTimeBySize;

    private List<ColumnTimeAvg> columnTimeAvg;

    private Chart<String, Double> leadTimeByType;

    private Chart<String, Long> tasksByType;

    private Chart<String, Double> leadTimeByProject;

    private Chart<String, Long> tasksByProject;

    private Chart<String, Double> leadTimeCompareChart;


}
