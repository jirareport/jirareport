package br.com.jiratorio.domain.request

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.validation.constraints.AssertFalse
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.NotNull

data class CreateIssuePeriodRequest(

    @JsonFormat(pattern = "dd/MM/yyyy")
    @field:NotNull(message = "Data de inicio é obrigatória")
    var startDate: LocalDate,

    @JsonFormat(pattern = "dd/MM/yyyy")
    @field:NotNull(message = "Data de fim é obrigatória")
    var endDate: LocalDate

) {

    @AssertFalse(message = "O período não pode ser maior que 31 dias. Para maiores períodos utilize o SandBox")
    fun isValidRange() =
        ChronoUnit.DAYS.between(startDate, endDate) > 31

    @AssertTrue(message = "A data de inicio deve ser anterior a data fim.")
    fun isStartDateIsBeforeEndDate() =
        startDate.isBefore(endDate) || startDate.isEqual(endDate)

}
