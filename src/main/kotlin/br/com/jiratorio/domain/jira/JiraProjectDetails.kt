package br.com.jiratorio.domain.jira

data class JiraProjectDetails(
    val id: Long,
    val name: String,
    val issueTypes: List<JiraIssueType>
)
