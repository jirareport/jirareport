package br.com.jiratorio.jira.client.config

import br.com.jiratorio.exception.BadCredentialsException
import br.com.jiratorio.internationalization.MessageResolver
import feign.codec.ErrorDecoder
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean

class AuthClientConfiguration(
    private val messageResolver: MessageResolver
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun errorDecoder() =
        ErrorDecoder { methodKey, response ->
            log.info("Method=errorDecoder, methodKey={}, response={}", methodKey, response)

            val headers = response.headers()
            val reason = headers["x-authentication-denied-reason"]?.first()

            BadCredentialsException(
                when {
                    reason.isNullOrBlank() -> messageResolver.resolve("login.fail")
                    reason.startsWith("CAPTCHA_CHALLENGE") -> messageResolver.resolve("login.fail.captcha-challenge")
                    else -> messageResolver.resolve("login.fail")
                }
            )
        }

}
