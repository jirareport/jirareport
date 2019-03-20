package br.com.jiratorio.config

import feign.Client
import java.net.URI
import java.net.URISyntaxException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignClientConfiguration(
    @param:Value("\${jira.url:}") private val jiraUrl: String,
    @param:Value("\${holiday.url:}") private val holidayUrl: String
) {

    @Bean
    fun feignClient() = Client.Default(
        NaiveSSLSocketFactory(
            URI(jiraUrl).host,
            URI(holidayUrl).host
        ),
        NoopHostnameVerifier()
    )

}
