package br.com.jiratorio.client

import br.com.jiratorio.HolidayClientConfig
import br.com.jiratorio.domain.response.holiday.HolidayApiResponse
import org.springframework.cache.annotation.Cacheable
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "holiday-client",
    url = "\${holiday.url}",
    configuration = [
        HolidayClientConfig::class
    ]
)
interface HolidayClient {

    @GetMapping
    @Cacheable("findAllHolidaysInCity")
    fun findAllHolidaysInCity(
        @RequestParam("ano") year: Int,
        @RequestParam("estado") state: String,
        @RequestParam("cidade") city: String,
        @RequestParam("token") token: String
    ): List<HolidayApiResponse>

}
