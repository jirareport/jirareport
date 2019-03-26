package br.com.jiratorio.service;

import br.com.jiratorio.domain.entity.Board;
import java.util.List;

import br.com.jiratorio.domain.entity.IssuePeriod;
import br.com.jiratorio.domain.request.CreateIssuePeriodRequest;
import br.com.jiratorio.domain.IssuePeriodChart;
import br.com.jiratorio.domain.response.IssuePeriodResponse;

public interface IssuePeriodService {

    Long create(CreateIssuePeriodRequest createIssuePeriodRequest, Long boardId);

    List<IssuePeriod> findByBoardId(Long boardId);

    IssuePeriodChart buildCharts(List<IssuePeriod> issues, Board board);

    IssuePeriod findById(Long issuePeriodId);

    void remove(Long issuePeriodId);

    void update(Long issuePeriodId);

    IssuePeriodResponse findIssuePeriodsAndCharts(Long boardId);

}
