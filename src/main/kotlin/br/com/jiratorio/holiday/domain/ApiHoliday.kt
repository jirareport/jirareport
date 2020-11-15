package br.com.jiratorio.holiday.domain

import br.com.jiratorio.domain.Holiday
import java.time.LocalDate

data class ApiHoliday(
    override val date: LocalDate,
    override val description: String,
) : Holiday
