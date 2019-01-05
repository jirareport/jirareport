package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.Board;
import java.util.List;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.form.IssuePeriodForm;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodChart;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodList;

public interface IssuePeriodService {

    void create(IssuePeriodForm issuePeriodForm, Long boardId);

    List<IssuePeriod> findByBoardId(Long boardId);

    IssuePeriodChart buildCharts(List<IssuePeriod> issues, Board board);

    IssuePeriod findById(Long issuePeriodId);

    void remove(Long issuePeriodId);

    void update(Long issuePeriodId);

    IssuePeriodList findIssuePeriodsAndCharts(Long boardId);

}
