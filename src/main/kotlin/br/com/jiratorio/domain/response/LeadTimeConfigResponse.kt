package br.com.jiratorio.domain.response

data class LeadTimeConfigResponse(
    val id: Long,
    val boardId: Long,
    val name: String,
    val startColumn: String,
    val endColumn: String
)
