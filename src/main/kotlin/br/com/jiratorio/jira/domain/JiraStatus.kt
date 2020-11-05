package br.com.jiratorio.jira.domain

data class JiraStatus(
    val self: String,
    val description: String,
    val iconUrl: String,
    val name: String,
    val id: String,
    val statusCategory: StatusCategory,
)
