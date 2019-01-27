package br.com.leonardoferreira.jirareport.domain.response;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodChart;
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
