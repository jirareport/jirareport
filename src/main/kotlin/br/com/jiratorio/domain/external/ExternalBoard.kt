package br.com.jiratorio.domain.external

data class ExternalBoard(
    val id: Long,
    val name: String,
    val issueTypes: List<ExternalIssueType> = emptyList(),
)
