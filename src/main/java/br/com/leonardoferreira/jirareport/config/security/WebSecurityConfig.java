package br.com.leonardoferreira.jirareport.config.security;

import java.io.IOException;
import java.nio.charset.Charset;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StreamUtils;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JiraAuthenticationProvider jiraAuthenticationProvider;

    @Autowired
    private TokenAuthenticationProvider tokenAuthenticationProvider;

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

    @Bean
    public RsaSigner rsaSigner(final ResourceLoader resourceLoader,
                               @Value("${ssh-key.private}") final String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        return new RsaSigner(
                StreamUtils.copyToString(resource.getInputStream(), Charset.forName("UTF-8")));
    }

    @Bean
    public RsaVerifier rsaVerifier(final ResourceLoader resourceLoader,
                                   @Value("${ssh-key.public}") final String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        return new RsaVerifier(
                StreamUtils.copyToString(resource.getInputStream(), Charset.forName("UTF-8")));
    }

}
