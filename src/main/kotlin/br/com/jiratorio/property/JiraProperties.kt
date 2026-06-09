package br.com.jiratorio.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jira")
data class JiraProperties(

    val url: String

)
