package br.com.jiratorio.service;

import br.com.jiratorio.domain.Board;
import java.util.List;

import br.com.jiratorio.domain.IssuePeriod;
import br.com.jiratorio.domain.form.IssuePeriodForm;
import br.com.jiratorio.domain.vo.IssuePeriodChart;
import br.com.jiratorio.domain.vo.IssuePeriodList;

public interface IssuePeriodService {

    Long create(IssuePeriodForm issuePeriodForm, Long boardId);

    List<IssuePeriod> findByBoardId(Long boardId);

    IssuePeriodChart buildCharts(List<IssuePeriod> issues, Board board);

    IssuePeriod findById(Long issuePeriodId);

    void remove(Long issuePeriodId);

    void update(Long issuePeriodId);

    IssuePeriodList findIssuePeriodsAndCharts(Long boardId);

}
