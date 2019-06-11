package br.com.jiratorio.config.properties

import br.com.jiratorio.config.properties.HolidayProperties
import br.com.jiratorio.config.properties.JiraProperties
import br.com.jiratorio.config.properties.SecurityProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    JiraProperties::class,
    HolidayProperties::class,
    SecurityProperties::class
)
class PropertiesConfiguration
