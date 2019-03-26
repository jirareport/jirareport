package br.com.jiratorio.domain.response;

import br.com.jiratorio.domain.chart.IssuePeriodChartResponse;
import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.entity.IssuePeriod;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListIssuePeriodResponse {

    private List<IssuePeriod> issuePeriods;

    private IssuePeriodChartResponse issuePeriodChart;

    private Board board;

}
