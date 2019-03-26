package br.com.jiratorio.exception;

import br.com.jiratorio.domain.jira.JiraError;
import lombok.Getter;

public class JiraException extends RuntimeException {
    private static final long serialVersionUID = 4765889713947456664L;

    @Getter
    private final JiraError jiraError;

    public JiraException(final JiraError jiraError) {
        super(jiraError.getMessage());
        this.jiraError = jiraError;
    }
}
