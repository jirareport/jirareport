package br.com.jiratorio.domain.request

import br.com.jiratorio.domain.estimate.EstimateFieldReference
import java.time.LocalDate

class SearchEstimateRequest(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val filter: EstimateFieldReference
)
