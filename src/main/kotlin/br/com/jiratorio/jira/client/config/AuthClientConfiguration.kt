package br.com.jiratorio.jira.client.config

import br.com.jiratorio.exception.BadCredentialsException
import br.com.jiratorio.internationalization.MessageResolver
import br.com.jiratorio.jira.client.AuthClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatusCode
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class AuthClientConfiguration(
    private val messageResolver: MessageResolver,
    @param:Value("\${jira.url}") private val jiraUrl: String,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun authClient(): AuthClient {
        val restClient = RestClient.builder()
            .baseUrl(jiraUrl)
            .defaultStatusHandler(HttpStatusCode::isError) { request, response ->
                log.info("Method=errorDecoder, status={}", response.statusCode.value())
                val reason = response.headers["x-authentication-denied-reason"]?.firstOrNull()
                throw BadCredentialsException(
                    when {
                        reason.isNullOrBlank() -> messageResolver.resolve("login.fail")
                        reason.startsWith("CAPTCHA_CHALLENGE") -> messageResolver.resolve("login.fail.captcha-challenge")
                        else -> messageResolver.resolve("login.fail")
                    }
                )
            }
            .build()

        val factory = HttpServiceProxyFactory.builderFor(
            RestClientAdapter.create(restClient)
        ).build()
        return factory.createClient(AuthClient::class.java)
    }

}
