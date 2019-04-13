package br.com.jiratorio.controller

import br.com.jiratorio.domain.estimate.EstimateFieldReference
import br.com.jiratorio.domain.form.EstimateForm
import br.com.jiratorio.domain.response.EstimateResponse
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.EstimateService
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/boards/{boardId}/estimates")
class EstimateController(
    private val boardService: BoardService,
    private val estimateService: EstimateService
) {

    @GetMapping
    fun index(@PathVariable boardId: Long, estimateForm: EstimateForm): EstimateResponse {
        val board = boardService.findById(boardId)

        val estimateIssueList = estimateService.findEstimateIssues(boardId, estimateForm)

        val estimateFieldReferenceList = EstimateFieldReference.retrieveCustomList(
            !StringUtils.isEmpty(board.systemCF),
            !StringUtils.isEmpty(board.estimateCF),
            !StringUtils.isEmpty(board.epicCF),
            !StringUtils.isEmpty(board.projectCF)
        )

        return EstimateResponse(
            estimateForm = estimateForm,
            board = board,
            estimateFieldReferenceList = estimateFieldReferenceList,
            estimateIssueList = estimateIssueList
        )
    }
}
