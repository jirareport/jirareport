package br.com.leonardoferreira.jirareport.exception;

import br.com.leonardoferreira.jirareport.domain.vo.JiraError;
import lombok.Getter;

/**
 * @author lferreira on 08/06/18
 */
public class JiraException extends RuntimeException {
    private static final long serialVersionUID = 4765889713947456664L;

    @Getter
    private final JiraError jiraError;

    public JiraException(final JiraError jiraError) {
        super(jiraError.getMessage());
        this.jiraError = jiraError;
    }
}
