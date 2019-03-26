package br.com.jiratorio.client.config

import br.com.jiratorio.domain.Account
import feign.RequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.security.core.context.SecurityContextHolder

class JiraClientConfiguration {

    @Bean
    fun requestInterceptor() = RequestInterceptor {
        val principal = SecurityContextHolder.getContext().authentication.principal as Account
        it.header("Authorization", principal.token)
    }

}
