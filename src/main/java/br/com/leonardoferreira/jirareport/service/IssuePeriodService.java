package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodChartVO;
import br.com.leonardoferreira.jirareport.exception.CreateIssuePeriodException;

import java.util.List;

/**
 * Created by lferreira on 3/26/18
 */
public interface IssuePeriodService {

    void create(IssuePeriodId issuePeriodId) throws CreateIssuePeriodException;

    List<IssuePeriod> findByProjectId(Long projectId);

    IssuePeriodChartVO getChartByIssues(List<IssuePeriod> issues);

    IssuePeriod findById(IssuePeriodId issuePeriodId);

    void remove(IssuePeriodId issuePeriodId);

    void update(IssuePeriodId issuePeriodId) throws CreateIssuePeriodException;

}
