package br.com.leonardoferreira.jirareport.helper;

import br.com.leonardoferreira.jirareport.domain.Issue;

/**
 * @author lferreira on 31/05/18
 */
public class ApplicationHelper {

    public String issueTitle(final Issue issue) {
        return String.format("%s %s", issue.getKey(), issue.getSummary());
    }

    public String fmtLeadTime(final Double leadTime) {
        return String.format("%.2f", leadTime);
    }
}
