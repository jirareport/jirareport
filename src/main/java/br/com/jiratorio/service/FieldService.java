package br.com.jiratorio.service;

import java.util.List;

import br.com.jiratorio.domain.JiraField;

public interface FieldService {

    List<JiraField> findAllJiraFields();

}
