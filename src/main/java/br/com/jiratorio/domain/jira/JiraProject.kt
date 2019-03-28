package br.com.jiratorio.domain.jira

data class JiraProject(
    val id: Long,
    val name: String,
    val issueTypes: List<JiraIssueType>
)
