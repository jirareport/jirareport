package br.com.jiratorio.client.config

import br.com.jiratorio.config.internationalization.MessageResolver
import feign.codec.ErrorDecoder
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.BadCredentialsException

class AuthClientConfiguration {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun errorDecoder(messageResolver: MessageResolver) =
        ErrorDecoder { methodKey, response ->
            log.info("Method=errorDecoder, methodKey={}, response={}", methodKey, response)

            val headers = response.headers()
            val reason = headers["x-authentication-denied-reason"]?.first()

            BadCredentialsException(
                when {
                    reason.isNullOrBlank() -> messageResolver("login.fail")
                    reason.startsWith("CAPTCHA_CHALLENGE") -> messageResolver("login.fail.captcha-challenge")
                    else -> messageResolver("login.fail")
                }
            )
        }

}
