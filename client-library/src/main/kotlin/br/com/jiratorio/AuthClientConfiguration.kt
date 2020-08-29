package br.com.jiratorio

import br.com.jiratorio.exception.BadCredentialsException
import feign.codec.ErrorDecoder
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean

class AuthClientConfiguration {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun errorDecoder() =
        ErrorDecoder { methodKey, response ->
            log.info("Method=errorDecoder, methodKey={}, response={}", methodKey, response)

            val headers = response.headers()
            val reason = headers["x-authentication-denied-reason"]?.first()

            BadCredentialsException(
                when {
                    reason.isNullOrBlank() -> "login.fail" // TODO: resolve in application module
                    reason.startsWith("CAPTCHA_CHALLENGE") -> "login.fail.captcha-challenge" // TODO: resolve in application module
                    else -> "login.fail" // TODO: resolve in application module
                }
            )
        }

}
