package br.com.jiratorio.jira

data class JiraProjectDetails(

    val id: Long,

    val name: String,

    val issueTypes: List<JiraIssueType>

)
