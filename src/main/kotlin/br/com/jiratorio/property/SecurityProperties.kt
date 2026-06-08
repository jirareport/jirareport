package br.com.jiratorio.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("security")
data class SecurityProperties(

    val key: String

)
