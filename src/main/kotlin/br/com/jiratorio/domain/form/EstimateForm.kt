package br.com.jiratorio.domain.form

import br.com.jiratorio.domain.estimate.EstimateFieldReference
import java.time.LocalDate

class EstimateForm(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val filter: EstimateFieldReference
)
