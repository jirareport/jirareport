package br.com.jiratorio.client.config

import br.com.jiratorio.config.internationalization.MessageResolver
import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.jira.JiraError
import br.com.jiratorio.exception.JiraException
import br.com.jiratorio.extension.account
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import feign.RequestInterceptor
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean
import org.springframework.security.core.context.SecurityContextHolder

class JiraClientConfiguration(
    private val objectMapper: ObjectMapper,
    private val messageResolver: MessageResolver
) {

    @Bean
    fun requestInterceptor() = RequestInterceptor {
        val principal: Account? = SecurityContextHolder.getContext().account
        if (principal != null) {
            it.header("Authorization", principal.token)
        }
    }

    @Bean
    fun errorDecoder() = ErrorDecoder { _, response ->
        JiraException(
            try {
                objectMapper.readValue(response.body().asInputStream(), JiraError::class.java)
            } catch (e: JsonParseException) {
                JiraError(messageResolver.resolve("errors.session-timeout"), response.status().toLong())
            }
        )
    }

}
