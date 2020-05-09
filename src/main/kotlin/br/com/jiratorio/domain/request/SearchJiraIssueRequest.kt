package br.com.jiratorio.domain.request

data class SearchJiraIssueRequest(
    val jql: String,
    val expand: List<String> = listOf("changelog"),
    val startAt: Int,
    val maxResults: Int
)
