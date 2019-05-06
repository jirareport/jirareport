package br.com.jiratorio.domain.response

data class IssueDetailResponse(
    val id: Long,
    val key: String,
    val changelog: List<ChangelogResponse>,
    val dueDateHistory: List<DueDateHistoryResponse>?,
    val leadTimes: Set<LeadTimeResponse>?,
    val waitTime: Double,
    val touchTime: Double,
    val pctEfficiency: Double
)
