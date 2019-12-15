package br.com.jiratorio.domain.request

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class HolidayRequest(

    @field:NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    val date: LocalDate,

    @field:NotBlank
    val description: String

)
