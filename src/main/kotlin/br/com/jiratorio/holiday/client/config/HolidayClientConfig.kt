package br.com.jiratorio.holiday.client.config

import br.com.jiratorio.holiday.client.HolidayClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.util.UriComponentsBuilder

@Configuration
class HolidayClientConfig(
    @Value("\${holiday.url}") private val holidayUrl: String,
) {

    @Bean
    fun holidayClient(): HolidayClient {
        val restClient = RestClient.builder()
            .baseUrl(holidayUrl)
            .requestInterceptor(jsonQueryInterceptor())
            .build()

        val factory = HttpServiceProxyFactory.builderFor(
            RestClientAdapter.create(restClient)
        ).build()
        return factory.createClient(HolidayClient::class.java)
    }

    private fun jsonQueryInterceptor() = ClientHttpRequestInterceptor { request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution ->
        val uri = UriComponentsBuilder.fromUri(request.uri)
            .queryParam("json", "true")
            .build(true)
            .toUri()
        val wrapper = object : HttpRequest by request {
            override fun getURI() = uri
        }
        execution.execute(wrapper, body)
    }

}
