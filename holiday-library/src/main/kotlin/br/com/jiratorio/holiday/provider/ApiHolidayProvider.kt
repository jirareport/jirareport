package br.com.jiratorio.holiday.provider

import br.com.jiratorio.domain.Holiday
import br.com.jiratorio.holiday.client.HolidayClient
import br.com.jiratorio.holiday.domain.ApiHoliday
import br.com.jiratorio.provider.HolidayProvider
import org.springframework.stereotype.Component

@Component
class ApiHolidayProvider(
    private val holidayClient: HolidayClient,
) : HolidayProvider {

    override fun findAllHolidays(year: Int, state: String, city: String, token: String): List<Holiday> =
        holidayClient.findAllHolidaysInCity(year, state, city, token)
            .map {
                ApiHoliday(
                    date = it.date,
                    description = it.name
                )
            }

}
