package br.com.leonardoferreira.jirareport.repository;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;

public interface IssueCustomRepository {

    List<Issue> findByExample(Long projectId, IssueForm issueForm);

}
