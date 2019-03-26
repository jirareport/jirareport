package br.com.jiratorio.domain.jira;

import java.util.List;

import lombok.Data;

@Data
public class JiraProject {

    private Long id;

    private String name;

    private List<JiraIssueType> issueTypes;

}
