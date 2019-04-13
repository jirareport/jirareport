package br.com.jiratorio.exception

import br.com.jiratorio.domain.jira.JiraError

class JiraException(jiraError: JiraError) : RuntimeException(jiraError.message) {
    companion object {
        private val serialVersionUID = 4765889713947456664L
    }
}
