package br.com.jiratorio.domain.response

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.estimate.EstimateFieldReference
import br.com.jiratorio.domain.estimate.EstimateIssue
import br.com.jiratorio.domain.form.EstimateForm

data class EstimateResponse(
    val estimateForm: EstimateForm,
    val board: Board,
    val estimateFieldReferenceList: List<EstimateFieldReference>,
    val estimateIssueList: List<EstimateIssue>
)
