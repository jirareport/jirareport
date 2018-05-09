package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.embedded.IssueForm;
import br.com.leonardoferreira.jirareport.domain.vo.IssuePeriodChartVO;
import br.com.leonardoferreira.jirareport.exception.CreateIssuePeriodException;

import java.util.List;

/**
 * Created by lferreira on 3/26/18
 */
public interface IssuePeriodService {

    void create(IssueForm issueForm) throws CreateIssuePeriodException;

    List<IssuePeriod> findByProjectId(Long projectId);

    IssuePeriodChartVO getChartByIssues(List<IssuePeriod> issues);

    IssuePeriod findById(IssueForm issueForm);

    void remove(IssueForm issueForm);

    void update(IssueForm issueForm) throws CreateIssuePeriodException;

}
