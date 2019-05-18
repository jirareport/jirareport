package br.com.jiratorio.domain.response.issue

import br.com.jiratorio.domain.response.ChangelogResponse
import br.com.jiratorio.domain.response.DueDateHistoryResponse
import br.com.jiratorio.domain.response.LeadTimeResponse

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
