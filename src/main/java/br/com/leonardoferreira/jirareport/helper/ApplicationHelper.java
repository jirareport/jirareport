package br.com.leonardoferreira.jirareport.helper;

import br.com.leonardoferreira.jirareport.domain.ImpedimentType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import br.com.leonardoferreira.jirareport.domain.Issue;

/**
 * @author lferreira on 31/05/18
 */
public class ApplicationHelper {

    private final String jiraUrl;

    public ApplicationHelper(final String url) {
        jiraUrl = url;
    }

    public String issueTitle(final Issue issue) {
        return String.format("%s %s", issue.getKey(), issue.getSummary());
    }

    public String fmtLeadTime(final Double leadTime) {
        return String.format("%.2f", leadTime);
    }

    public String fmtLocalDate(final LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public ImpedimentType[] impedimentTypes() {
        return ImpedimentType.values();
    }

    public String buildJiraLink(final String key) {
        return String.format("%s/browse/%s", jiraUrl, key);
    }
}
