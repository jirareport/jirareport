package br.com.leonardoferreira.jirareport.service;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import br.com.leonardoferreira.jirareport.domain.form.IssuePeriodForm;
import br.com.leonardoferreira.jirareport.domain.vo.SandBox;
import br.com.leonardoferreira.jirareport.domain.vo.SandBoxFilter;

/**
 * @author lferreira
 * @since 5/7/18 8:01 PM
 */
public interface IssueService {

    List<Issue> findAllInJira(IssuePeriodForm issuePeriodId, Long boardId);

    SandBox findByExample(Long boardId, IssueForm issueForm);

    SandBoxFilter findSandBoxFilters(Long boardId, SandBox sandBox, IssueForm issueForm);

    List<Issue> findByIssuePeriodId(Long issuePeriodId);
}
