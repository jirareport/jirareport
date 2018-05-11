package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;

import java.util.List;

/**
 * @author s2it_leferreira
 * @since 5/7/18 8:01 PM
 */
public interface IssueService {

    List<Issue> findAllInJira(IssuePeriodId issuePeriodId);

    List<Issue> findByExample(Long projectId, IssueForm issueForm);

    void saveAll(List<Issue> issues);
}
