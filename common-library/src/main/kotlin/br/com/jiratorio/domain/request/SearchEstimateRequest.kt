package br.com.jiratorio.domain.request

import br.com.jiratorio.domain.EstimateFieldReference
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.AssertTrue

class SearchEstimateRequest(

    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val startDate: LocalDate,

    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val endDate: LocalDate,

    val filter: EstimateFieldReference

) {

    @AssertTrue(message = "{validations.start-date-cant-be-before-end-date}")
    fun isStartDateIsBeforeEndDate(): Boolean =
        startDate.isBefore(endDate) || startDate.isEqual(endDate)

}
