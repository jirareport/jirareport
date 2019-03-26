package br.com.jiratorio.service;

import br.com.jiratorio.domain.estimate.EstimateIssue;
import br.com.jiratorio.domain.sandbox.SandBox;
import br.com.jiratorio.domain.sandbox.SandBoxFilter;
import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.entity.Issue;
import br.com.jiratorio.domain.form.IssueForm;
import java.util.List;

public interface IssueService {

    List<Issue> createByJql(String jql, Board board);

    List<EstimateIssue> findEstimateByJql(String jql, Board board);

    SandBox findByExample(Long boardId, IssueForm issueForm);

    List<Long> findLeadTimeByExample(Long boardId, IssueForm issueForm);

    SandBoxFilter findSandBoxFilters(Long boardId, SandBox sandBox, IssueForm issueForm);

    List<Issue> findByIssuePeriodId(Long issuePeriodId);

    void deleteAll(List<Issue> issues);
}
