package br.com.jiratorio.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("security")
class SecurityProperties {

    lateinit var key: String

}
