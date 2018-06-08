package br.com.leonardoferreira.jirareport.service;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.vo.JiraField;

/**
 * @author lferreira on 07/06/18
 */
public interface FieldService {

    List<JiraField> findAllJiraFields();

}
