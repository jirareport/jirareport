package br.com.jiratorio.domain.issue

data class FindIssueResult(
    val query: String,
    val issues: List<JiraIssue>,
)
