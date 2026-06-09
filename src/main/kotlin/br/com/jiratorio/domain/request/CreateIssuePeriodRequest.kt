package br.com.jiratorio.domain.request

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import jakarta.validation.constraints.AssertFalse
import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.NotNull

data class CreateIssuePeriodRequest(

    @JsonFormat(pattern = "dd/MM/yyyy")
    @field:NotNull(message = "{validations.required-start-date}")
    val startDate: LocalDate,

    @JsonFormat(pattern = "dd/MM/yyyy")
    @field:NotNull(message = "{validations.required-end-date}")
    val endDate: LocalDate

) {

    @AssertFalse(message = "{validations.invalid-period-range}")
    fun isValidRange() =
        ChronoUnit.DAYS.between(startDate, endDate) > 31

    @AssertTrue(message = "{validations.start-date-cant-be-before-end-date}")
    fun isStartDateIsBeforeEndDate() =
        startDate.isBefore(endDate) || startDate.isEqual(endDate)

}
