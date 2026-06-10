package br.com.jiratorio.jira.client.config

import br.com.jiratorio.domain.CurrentUser
import br.com.jiratorio.exception.UnauthorizedException
import br.com.jiratorio.internationalization.MessageResolver
import br.com.jiratorio.jira.client.FieldClient
import br.com.jiratorio.jira.client.IssueClient
import br.com.jiratorio.jira.client.ProjectClient
import br.com.jiratorio.jira.domain.JiraError
import br.com.jiratorio.jira.domain.exception.JiraException
import br.com.jiratorio.jira.domain.exception.JiraNotFoundException
import tools.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatusCode
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class JiraClientConfiguration(
    private val objectMapper: ObjectMapper,
    private val currentUser: CurrentUser,
    private val messageResolver: MessageResolver,
    @param:Value("\${jira.url}") private val jiraUrl: String,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    private fun buildJiraRestClient(suppressNotFound: Boolean = false): RestClient {
        val builder = RestClient.builder()
            .baseUrl(jiraUrl)
            .requestInterceptor(jiraAuthInterceptor())
            .defaultStatusHandler(HttpStatusCode::is4xxClientError) { request, response ->
                val status = response.statusCode.value()
                if (status == 404 && suppressNotFound) {
                    throw JiraNotFoundException()
                }
                if (status == 401) {
                    throw UnauthorizedException()
                }
                throw JiraException(
                    try {
                        objectMapper.readValue(response.body, JiraError::class.java)
                    } catch (e: Exception) {
                        log.error("Method=errorDecoder, status={}", status, e)
                        JiraError(messageResolver.resolve("errors.session-timeout"), status.toLong())
                    }
                )
            }
            .defaultStatusHandler(HttpStatusCode::is5xxServerError) { _, response ->
                val status = response.statusCode.value()
                throw JiraException(
                    try {
                        objectMapper.readValue(response.body, JiraError::class.java)
                    } catch (e: Exception) {
                        log.error("Method=errorDecoder, status={}", status, e)
                        JiraError(messageResolver.resolve("errors.session-timeout"), status.toLong())
                    }
                )
            }
        return builder.build()
    }

    private fun jiraAuthInterceptor() = ClientHttpRequestInterceptor { request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution ->
        request.headers.set("Authorization", currentUser.jiraToken)
        request.headers.set("Accept-Language", "en")
        request.headers.set("X-Force-Accept-Language", "true")
        execution.execute(request, body)
    }

    @Bean
    fun fieldClient(): FieldClient {
        val factory = HttpServiceProxyFactory.builderFor(
            RestClientAdapter.create(buildJiraRestClient())
        ).build()
        return factory.createClient(FieldClient::class.java)
    }

    @Bean
    fun issueClient(): IssueClient {
        val factory = HttpServiceProxyFactory.builderFor(
            RestClientAdapter.create(buildJiraRestClient())
        ).build()
        return factory.createClient(IssueClient::class.java)
    }

    @Bean
    fun projectClient(): ProjectClient {
        val factory = HttpServiceProxyFactory.builderFor(
            RestClientAdapter.create(buildJiraRestClient(suppressNotFound = true))
        ).build()
        return factory.createClient(ProjectClient::class.java)
    }

}
