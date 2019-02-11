package br.com.jiratorio.domain.response;

import br.com.jiratorio.domain.vo.IssuePeriodChart;
import br.com.jiratorio.domain.Board;
import br.com.jiratorio.domain.IssuePeriod;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListIssuePeriodResponse {

    private List<IssuePeriod> issuePeriods;

    private IssuePeriodChart issuePeriodChart;

    private Board board;

}
