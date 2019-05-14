package br.com.jiratorio.domain.request

import br.com.jiratorio.domain.estimate.EstimateFieldReference
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

class SearchEstimateRequest(

    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val startDate: LocalDate,

    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val endDate: LocalDate,

    val filter: EstimateFieldReference

)
