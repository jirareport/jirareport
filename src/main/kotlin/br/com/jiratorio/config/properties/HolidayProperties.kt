package br.com.jiratorio.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "holiday")
class HolidayProperties {

    lateinit var url: String

    lateinit var token: String

}
