package br.com.jiratorio.service

import br.com.jiratorio.domain.jira.JiraField

interface FieldService {

    fun findAllJiraFields(): List<JiraField>

}
