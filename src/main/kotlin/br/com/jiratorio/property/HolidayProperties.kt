package br.com.jiratorio.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "holiday")
data class HolidayProperties(

    val url: String,

    val defaultState: String,

    val defaultCity: String,

    val token: String

)
