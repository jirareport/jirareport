package br.com.jiratorio.domain.response;

import br.com.jiratorio.domain.Board;
import br.com.jiratorio.domain.Issue;
import br.com.jiratorio.domain.IssuePeriod;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IssuePeriodDetailsResponse {

    private IssuePeriod issuePeriod;

    private List<Issue> issues;

    private Board board;

}
