package br.com.leonardoferreira.jirareport.client;

import br.com.leonardoferreira.jirareport.domain.Issue;
import br.com.leonardoferreira.jirareport.domain.form.IssueForm;

import java.util.List;

/**
 * Created by lferreira on 3/26/18
 */
public interface IssueClient {
    List<Issue> findAll(String token, IssueForm issueForm);
}
