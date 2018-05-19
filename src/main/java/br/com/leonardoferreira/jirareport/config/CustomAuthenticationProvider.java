package br.com.leonardoferreira.jirareport.config;

import java.util.Collections;

import br.com.leonardoferreira.jirareport.domain.form.LoginForm;
import br.com.leonardoferreira.jirareport.domain.vo.Account;
import br.com.leonardoferreira.jirareport.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * @author lferreira
 * @since 11/14/17 3:30 PM
 */
@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AuthService authService;

    @Override
    public Authentication authenticate(final Authentication auth) {
        try {
            Account login = authService.login(new LoginForm(auth.getName(), auth.getCredentials().toString()));
            if (login == null) {
                throw new BadCredentialsException("External system authentication failed");
            }

            return new UsernamePasswordAuthenticationToken(login, null, Collections.emptyList());
        } catch (Exception e) {
            log.error("Method=authenticate, E={}", e.getMessage(), e);
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(final Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}
