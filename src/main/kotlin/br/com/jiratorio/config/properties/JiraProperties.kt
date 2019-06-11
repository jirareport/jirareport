package br.com.jiratorio.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jira")
class JiraProperties {

    lateinit var url: String

}
