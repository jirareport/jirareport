package br.com.jiratorio.domain.response.issueperiod

import br.com.jiratorio.domain.response.IssueResponse

data class IssuePeriodByIdResponse(
    val detail: IssuePeriodDetailResponse,
    val issues: List<IssueResponse>
)
