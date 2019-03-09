package br.com.jiratorio.service;

import br.com.jiratorio.domain.entity.Board;
import br.com.jiratorio.domain.entity.Issue;
import br.com.jiratorio.domain.form.IssueForm;
import br.com.jiratorio.domain.form.IssuePeriodForm;
import br.com.jiratorio.domain.EstimateIssue;
import br.com.jiratorio.domain.SandBox;
import br.com.jiratorio.domain.SandBoxFilter;

import java.util.List;

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
