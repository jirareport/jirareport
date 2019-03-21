package br.com.jiratorio.dsl.jira

class JiraIssueType(
    val id: Int = 1,
    val self: String = "http://localhost:8888/jira/rest/api/2/issuetype/$id",
    val description: String = "",
    val iconUrl: String = "http://localhost:8888/jira/secure/viewavatar?size=xsmall&avatarId=$id&avatarType=issuetype",
    val name: String = "Task",
    val subtask: Boolean = false,
    val avatarId: Int = 1
)
