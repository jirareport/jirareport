package br.com.jiratorio.client.config

import br.com.jiratorio.config.internationalization.MessageResolver
import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.jira.JiraError
import br.com.jiratorio.exception.JiraException
import br.com.jiratorio.exception.UnauthorizedException
import br.com.jiratorio.extension.account
import com.fasterxml.jackson.databind.ObjectMapper
import feign.RequestInterceptor
import feign.codec.ErrorDecoder
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.security.core.context.SecurityContextHolder
import javax.servlet.http.HttpServletResponse

class JiraClientConfiguration(
    private val objectMapper: ObjectMapper,
    private val messageResolver: MessageResolver
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun requestInterceptor() = RequestInterceptor {
        val principal: Account? = SecurityContextHolder.getContext().account
        if (principal != null) {
            it.header("Authorization", principal.token)
        }
    }

    @Bean
    fun errorDecoder() = ErrorDecoder { methodKey, response ->
        log.info("Method=errorDecoder, methodKey={}, response={}", methodKey, response)

        if (response.status() == HttpServletResponse.SC_UNAUTHORIZED) {
            UnauthorizedException()
        } else {
            JiraException(
                try {
                    objectMapper.readValue(response.body().asInputStream(), JiraError::class.java)
                } catch (e: Exception) {
                    log.error("Method=errorDecoder, methodKey={}, response={}", methodKey, response, e)
                    JiraError(messageResolver.resolve("errors.session-timeout"), response.status().toLong())
                }
            )
        }
    }

}
