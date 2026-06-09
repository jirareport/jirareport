package br.com.jiratorio.holiday.client

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange
interface HolidayClient {

    @GetExchange("/api/data.php")
    fun findAllHolidaysInCity(
        @RequestParam("ano") year: Int,
        @RequestParam("estado") state: String,
        @RequestParam("cidade") city: String,
    ): String

}
