package br.com.jiratorio.domain.response.holiday

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class HolidayApiResponse(
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    val date: LocalDate,

    val name: String,

    val description: String

)
