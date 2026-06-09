package br.com.jiratorio.holiday.provider

import br.com.jiratorio.domain.Holiday
import br.com.jiratorio.holiday.client.HolidayClient
import br.com.jiratorio.provider.HolidayProvider
import org.springframework.stereotype.Component
import java.text.Normalizer

@Component
class ApiHolidayProvider(
    private val holidayClient: HolidayClient,
    private val holidayResponseParser: HolidayResponseParser,
) : HolidayProvider {

    override fun findAllHolidays(year: Int, state: String, city: String, token: String): List<Holiday> {
        val normalizedCity = normalizeCity(city)
        val body = holidayClient.findAllHolidaysInCity(year, state, normalizedCity)
        return holidayResponseParser.parse(body)
    }

    private fun normalizeCity(city: String): String =
        Normalizer.normalize(city, Normalizer.Form.NFD)
            .replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
            .replace("['‘’]".toRegex(), "")
            .uppercase()
            .trim()

}
