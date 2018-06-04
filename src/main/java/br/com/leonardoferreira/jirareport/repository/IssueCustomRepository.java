package br.com.leonardoferreira.jirareport.repository;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;
import java.util.List;

public interface IssueCustomRepository {

    List<Issue> findByExample(Long boardId, IssueForm issueForm);

}
