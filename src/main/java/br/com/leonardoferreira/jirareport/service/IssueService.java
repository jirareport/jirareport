package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.Board;
import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.form.IssuePeriodForm;
import br.com.leonardoferreira.jirareport.domain.vo.EstimateIssue;
import br.com.leonardoferreira.jirareport.domain.vo.SandBox;
import br.com.leonardoferreira.jirareport.domain.vo.SandBoxFilter;

import java.util.List;

/**
 * @author lferreira
 * @since 5/7/18 8:01 PM
 */
public interface IssueService {

    List<Issue> createByJql(String jql, Board board);

    List<EstimateIssue> findEstimateByJql(String jql, Board board);

    SandBox findByExample(Long boardId, IssueForm issueForm);

    List<Long> findLeadTimeByExample(Long boardId, IssueForm issueForm);

    SandBoxFilter findSandBoxFilters(Long boardId, SandBox sandBox, IssueForm issueForm);

    List<Issue> findByIssuePeriodId(Long issuePeriodId);

    String searchJQL(IssuePeriodForm issuePeriodForm, Board board);

    void deleteAll(List<Issue> issues);
}
