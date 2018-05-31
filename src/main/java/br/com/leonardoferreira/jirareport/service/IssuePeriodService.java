package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.Board;
import java.util.List;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodChart;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodList;
import br.com.leonardoferreira.jirareport.exception.CreateIssuePeriodException;

/**
 * Created by lferreira on 3/26/18
 */
public interface IssuePeriodService {

    void create(IssuePeriodId issuePeriodId) throws CreateIssuePeriodException;

    List<IssuePeriod> findByBoardId(Long boardId);

    IssuePeriodChart buildCharts(List<IssuePeriod> issues, Board board);

    IssuePeriod findById(IssuePeriodId issuePeriodId);

    void remove(IssuePeriodId issuePeriodId);

    void update(IssuePeriodId issuePeriodId) throws CreateIssuePeriodException;

    IssuePeriodList findIssuePeriodsAndCharts(Long boardId);

}
