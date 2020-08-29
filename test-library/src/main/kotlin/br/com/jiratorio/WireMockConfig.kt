package br.com.jiratorio

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WireMockConfig {

    @Bean
    fun wireMockServer(): WireMockServer {
        val wireMockServer = WireMockServer(
            WireMockConfiguration
                .options()
                .port(8888)
        )
        wireMockServer.start()

        WireMock.configureFor(wireMockServer.port())

        return wireMockServer
    }

}
