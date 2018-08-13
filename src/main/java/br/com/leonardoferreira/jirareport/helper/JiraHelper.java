package br.com.leonardoferreira.jirareport.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JiraHelper implements Helper {

    @Value("${jira.url}")
    private String jiraUrl;

    @Override
    public String getName() {
        return "jiraHelper";
    }

    public String issueLink(final String key) {
        return String.format("%s/browse/%s", jiraUrl, key);
    }

    public String searchIssuesLink(final String jql) {
        return String.format("%s/issues/?jql=%s", jiraUrl, jql);
    }

}
