package br.com.jiratorio.security

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {

    @Bean
    fun corsConfiguration(): CorsConfiguration {
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowCredentials = true
        corsConfiguration.addAllowedOrigin("*")
        corsConfiguration.addAllowedHeader("*")
        listOf("X-Auth-Token", "X-Auth-Fail-Reason", "Location").forEach {
            corsConfiguration.addExposedHeader(it)
        }
        corsConfiguration.addAllowedMethod("*")

        return corsConfiguration
    }

    @Bean
    fun corsFilter(corsConfiguration: CorsConfiguration): FilterRegistrationBean<CorsFilter> {
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfiguration)

        val filter = FilterRegistrationBean<CorsFilter>(CorsFilter(source))
        filter.order = 0

        return filter
    }

}
