package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;

import br.com.leonardoferreira.jirareport.domain.vo.Histogram;
import br.com.leonardoferreira.jirareport.domain.vo.SandBox;
import br.com.leonardoferreira.jirareport.domain.vo.SandBoxFilter;
import java.util.List;

/**
 * @author lferreira
 * @since 5/7/18 8:01 PM
 */
public interface IssueService {

    List<Issue> findAllInJira(IssuePeriodId issuePeriodId);

    SandBox findByExample(Long projectId, IssueForm issueForm);

    SandBoxFilter findSandBoxFilters(Long projectId, SandBox sandBox, IssueForm issueForm);

    Histogram calcHistogramData(List<Issue> issues);

    List<Issue> findByIssuePeriodId(IssuePeriodId issuePeriodId);
}
