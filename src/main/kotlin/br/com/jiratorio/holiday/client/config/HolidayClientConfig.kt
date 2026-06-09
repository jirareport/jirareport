package br.com.jiratorio.holiday.client.config

import br.com.jiratorio.holiday.client.HolidayClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class HolidayClientConfig(
    @Value("\${holiday.url}") private val holidayUrl: String,
) {

    @Bean
    fun holidayClient(): HolidayClient {
        val restClient = RestClient.builder()
            .baseUrl(holidayUrl)
            .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
            .build()

        val factory = HttpServiceProxyFactory.builderFor(
            RestClientAdapter.create(restClient)
        ).build()
        return factory.createClient(HolidayClient::class.java)
    }

}
