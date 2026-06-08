package br.com.jiratorio.security

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val jiraAuthenticationProvider: JiraAuthenticationProvider,
    private val tokenAuthenticationProvider: TokenAuthenticationProvider,
    private val corsConfiguration: CorsConfiguration
) {

    @Bean
    fun authenticationManager(): AuthenticationManager =
        ProviderManager(listOf(jiraAuthenticationProvider, tokenAuthenticationProvider))

    @Bean
    fun securityFilterChain(http: HttpSecurity, authenticationManager: AuthenticationManager): SecurityFilterChain {
        with(http) {
            authorizeHttpRequests {
                it.requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                it.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                it.anyRequest().authenticated()
            }

            sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            formLogin {
                it.successHandler { _, response, authentication ->
                    response.status = HttpServletResponse.SC_OK
                    response.setHeader("X-Auth-Token", authentication.details.toString())
                }
                it.failureHandler { _, response, exception ->
                    response.setHeader("X-Auth-Fail-Reason", exception.message)
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST)
                }
                it.permitAll()
            }

            exceptionHandling {
                it.authenticationEntryPoint { _, response, _ ->
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                }
            }

            anonymous {
                it.disable()
            }

            httpBasic {
                it.disable()
            }

            csrf {
                it.disable()
            }

            cors {
                it.configurationSource { corsConfiguration }
            }

            addFilterBefore(AuthenticationFilter(authenticationManager), BasicAuthenticationFilter::class.java)
        }

        return http.build()
    }
}
