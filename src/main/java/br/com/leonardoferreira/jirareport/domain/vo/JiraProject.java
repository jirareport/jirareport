package br.com.leonardoferreira.jirareport.domain.vo;

import java.util.List;

import lombok.Data;

@Data
public class JiraProject {

    private Long id;

    private String name;

    private List<JiraIssueType> issueTypes;

}
