package br.com.jiratorio.jira.domain

data class JiraStatusList(
    val self: String,
    val id: String,
    val name: String,
    val subtask: String,
    val statuses: List<JiraStatus>
)
