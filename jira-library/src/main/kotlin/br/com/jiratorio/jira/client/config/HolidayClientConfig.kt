package br.com.jiratorio.jira.client.config

import feign.RequestInterceptor
import org.springframework.context.annotation.Bean

class HolidayClientConfig {

    @Bean
    fun requestInterceptor() = RequestInterceptor {
        it.query("json", "true")
    }

}
