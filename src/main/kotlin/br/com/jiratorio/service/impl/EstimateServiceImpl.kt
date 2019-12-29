package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.request.SearchEstimateRequest
import br.com.jiratorio.domain.response.EstimateIssueResponse
import br.com.jiratorio.service.EstimateService
import br.com.jiratorio.usecase.issue.estimate.EstimateIssue
import org.springframework.stereotype.Service

@Service
class EstimateServiceImpl(
    private val estimateIssue: EstimateIssue
) : EstimateService {

    override fun findEstimateIssues(
        boardId: Long,
        searchEstimateRequest: SearchEstimateRequest
    ): List<EstimateIssueResponse> =
        estimateIssue.execute(boardId, searchEstimateRequest)

}
