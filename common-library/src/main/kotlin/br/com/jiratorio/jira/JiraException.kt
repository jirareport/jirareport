package br.com.jiratorio.jira

class JiraException(val jiraError: JiraError) : RuntimeException(jiraError.message)
