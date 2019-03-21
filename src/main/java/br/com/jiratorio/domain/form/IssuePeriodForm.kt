package br.com.jiratorio.domain.form

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.validation.constraints.AssertFalse
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.NotNull

data class IssuePeriodForm(

    @JsonFormat(pattern = "dd/MM/yyyy")
    @field:NotNull(message = "Data de inicio é obrigatória")
    var startDate: LocalDate? = null,

    @JsonFormat(pattern = "dd/MM/yyyy")
    @field:NotNull(message = "Data de fim é obrigatória")
    var endDate: LocalDate? = null

) {

    @AssertFalse(message = "O período não pode ser maior que 31 dias. Para maiores períodos utilize o SandBox")
    fun isValidRange() =
        endDate != null && ChronoUnit.DAYS.between(startDate, endDate) > 31

    @AssertTrue(message = "A data de inicio deve ser anterior a data fim.")
    fun isStartDateIsBeforeEndDate() =
        startDate?.isBefore(endDate) == true || startDate?.isEqual(endDate) == true

}
