package br.com.jiratorio.service

import br.com.jiratorio.domain.estimate.EstimateIssue
import br.com.jiratorio.domain.form.EstimateForm

interface EstimateService {

    fun findEstimateIssues(boardId: Long, estimateForm: EstimateForm): List<EstimateIssue>

}
