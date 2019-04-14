package br.com.jiratorio.service

import br.com.jiratorio.domain.estimate.EstimateIssue
import br.com.jiratorio.domain.request.SearchEstimateRequest

interface EstimateService {

    fun findEstimateIssues(boardId: Long, searchEstimateRequest: SearchEstimateRequest): List<EstimateIssue>

}
