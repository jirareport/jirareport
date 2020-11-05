package br.com.jiratorio.controller

import br.com.jiratorio.domain.request.SearchEstimateRequest
import br.com.jiratorio.domain.response.EstimateIssueResponse
import br.com.jiratorio.service.EstimateIssueService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/boards/{boardId}/estimates")
class EstimateController(
    private val estimateIssue: EstimateIssueService
) {

    @GetMapping
    fun index(
        @PathVariable boardId: Long,
        searchEstimateRequest: SearchEstimateRequest
    ): List<EstimateIssueResponse> =
        estimateIssue.findAll(boardId, searchEstimateRequest)

}
