package br.com.leonardoferreira.jirareport.service;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.vo.JiraField;

public interface FieldService {

    List<JiraField> findAllJiraFields();

}
