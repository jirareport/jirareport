package br.com.jiratorio.domain.vo;

import br.com.jiratorio.domain.embedded.Chart;
import br.com.jiratorio.domain.embedded.ColumnTimeAvg;
import br.com.jiratorio.domain.embedded.Histogram;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    private Chart<String, Double> leadTimeByPriority;

    private Chart<String, Long> throughputByPriority;

    private List<DynamicChart> dynamicCharts;

}
