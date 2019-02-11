package br.com.jiratorio.config.security;

import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JiraAuthenticationProvider jiraAuthenticationProvider;

    private final TokenAuthenticationProvider tokenAuthenticationProvider;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeRequests()
                    .anyRequest().authenticated()
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin()
                    .successHandler((request, response, authentication) -> {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setHeader("X-Auth-Token", authentication.getDetails().toString());
                    })
                    .failureHandler((request, response, exception) -> {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getMessage());
                    })
                    .permitAll()
                .and()
                .exceptionHandling()
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    })
                .and()
                .anonymous()
                    .disable()
                .httpBasic()
                    .disable()
                .csrf()
                    .disable()
                .cors()
                    .disable()
                .addFilterBefore(new AuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
        // @formatter:on
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jiraAuthenticationProvider)
                .authenticationProvider(tokenAuthenticationProvider);
    }

}
