package br.com.jiratorio.service

import br.com.jiratorio.domain.request.SearchEstimateRequest
import br.com.jiratorio.domain.response.EstimateIssueResponse

interface EstimateService {

    fun findEstimateIssues(boardId: Long, searchEstimateRequest: SearchEstimateRequest): List<EstimateIssueResponse>

}
