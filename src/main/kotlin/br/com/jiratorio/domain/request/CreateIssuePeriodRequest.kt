package br.com.jiratorio.domain.request

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.validation.constraints.AssertFalse
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.NotNull

data class CreateIssuePeriodRequest(

    @JsonFormat(pattern = "dd/MM/yyyy")
    @field:NotNull(message = "{validations.required-start-date}")
    var startDate: LocalDate,

    @JsonFormat(pattern = "dd/MM/yyyy")
    @field:NotNull(message = "{validations.required-end-date}")
    var endDate: LocalDate

) {

    @AssertFalse(message = "{validations.invalid-period-range}")
    fun isValidRange() =
        ChronoUnit.DAYS.between(startDate, endDate) > 31

    @AssertTrue(message = "{validations.start-date-cant-be-before-end-date}")
    fun isStartDateIsBeforeEndDate() =
        startDate.isBefore(endDate) || startDate.isEqual(endDate)

}
