package br.com.jiratorio.domain.request

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class HolidayRequest(

    @field:NotNull
    @field:JsonFormat(pattern = "dd/MM/yyyy")
    val date: LocalDate,

    @field:NotBlank
    val description: String

)
