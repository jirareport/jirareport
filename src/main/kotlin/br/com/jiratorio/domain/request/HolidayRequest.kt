package br.com.jiratorio.domain.request

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate
import javax.validation.constraints.NotBlank

data class HolidayRequest(

    @JsonFormat(pattern = "dd/MM/yyyy")
    var date: LocalDate,

    @field:NotBlank
    var description: String

)
