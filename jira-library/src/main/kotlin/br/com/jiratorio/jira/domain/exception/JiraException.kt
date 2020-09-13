package br.com.jiratorio.jira.domain.exception

import br.com.jiratorio.jira.domain.JiraError

class JiraException(jiraError: JiraError) : RuntimeException(jiraError.message)
