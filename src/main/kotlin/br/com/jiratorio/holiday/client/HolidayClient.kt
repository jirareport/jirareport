package br.com.jiratorio.holiday.client

import br.com.jiratorio.holiday.domain.HolidayApiResponse
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange
interface HolidayClient {

    @GetExchange
    fun findAllHolidaysInCity(
        @RequestParam("ano") year: Int,
        @RequestParam("estado") state: String,
        @RequestParam("cidade") city: String,
        @RequestParam("token") token: String,
    ): List<HolidayApiResponse>

}
