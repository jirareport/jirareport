package br.com.jiratorio.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "holiday")
data class HolidayProperties(

    val url: String,

    val defaultState: String,

    val defaultCity: String,

    val decodeKey: String,

    val includedTypeCodes: Set<Int>

)
