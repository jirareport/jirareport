package br.com.jiratorio.security

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val jiraAuthenticationProvider: JiraAuthenticationProvider,
    private val tokenAuthenticationProvider: TokenAuthenticationProvider,
    private val corsConfiguration: CorsConfiguration
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        with(http) {
            authorizeRequests {
                it.antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                it.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
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

            addFilterBefore(AuthenticationFilter(authenticationManager()), BasicAuthenticationFilter::class.java)
        }
    }

    override fun configure(web: WebSecurity) {
        web.ignoring()
            .antMatchers(HttpMethod.GET, "/actuator/**")
            .antMatchers(HttpMethod.OPTIONS, "/**")
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(jiraAuthenticationProvider)
            .authenticationProvider(tokenAuthenticationProvider)
    }

}
