package br.com.jiratorio.exception

import br.com.jiratorio.domain.jira.JiraError

class JiraException(val jiraError: JiraError) : RuntimeException(jiraError.message)
