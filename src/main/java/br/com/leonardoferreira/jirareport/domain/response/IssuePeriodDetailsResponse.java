package br.com.leonardoferreira.jirareport.domain.response;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
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
