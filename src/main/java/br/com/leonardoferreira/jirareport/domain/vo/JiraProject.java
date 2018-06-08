package br.com.leonardoferreira.jirareport.domain.vo;

import java.util.List;

import lombok.Data;

/**
 * @author lferreira on 30/05/18
 */
@Data
public class JiraProject {

    private Long id;

    private String name;

    private List<JiraIssueType> issueTypes;

}
