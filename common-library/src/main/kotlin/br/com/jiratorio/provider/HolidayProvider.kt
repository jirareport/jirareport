package br.com.jiratorio.provider

import br.com.jiratorio.domain.Holiday

interface HolidayProvider {

    fun findAllHolidays(year: Int, state: String, city: String, token: String): List<Holiday>

}
