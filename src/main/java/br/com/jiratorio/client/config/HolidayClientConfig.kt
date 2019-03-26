package br.com.jiratorio.client.config

import feign.RequestInterceptor
import org.springframework.context.annotation.Bean

class HolidayClientConfig {

    @Bean
    fun requestInterceptor() = RequestInterceptor {
        it.query("json", "true")
    }

}
