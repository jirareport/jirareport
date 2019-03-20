package br.com.jiratorio.config.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val jiraAuthenticationProvider: JiraAuthenticationProvider,
    private val tokenAuthenticationProvider: TokenAuthenticationProvider
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        // @formatter:off
        http
            .authorizeRequests()
                .anyRequest().authenticated()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .formLogin()
                .successHandler { _, response, authentication ->
                    response.status = HttpServletResponse.SC_OK
                    response.setHeader("X-Auth-Token", authentication.details.toString())
                }
                .failureHandler { _, response, exception ->
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.message)
                }
            .permitAll()
            .and()
            .exceptionHandling()
                .authenticationEntryPoint { _, response, _ ->
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                }
            .and()
            .anonymous()
                .disable()
            .httpBasic()
                .disable()
            .csrf()
                .disable()
            .cors()
                .disable()
            .addFilterBefore(AuthenticationFilter(authenticationManager()), BasicAuthenticationFilter::class.java)
        // @formatter:on
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
            .authenticationProvider(jiraAuthenticationProvider)
            .authenticationProvider(tokenAuthenticationProvider)
    }

}
