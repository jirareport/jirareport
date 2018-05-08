package br.com.leonardoferreira.jirareport.config;

import java.util.Collections;

import br.com.leonardoferreira.jirareport.domain.form.AccountForm;
import br.com.leonardoferreira.jirareport.domain.vo.AccountVO;
import br.com.leonardoferreira.jirareport.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @author lferreira
 * @since 11/14/17 3:30 PM
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AuthService authService;

    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
        String username = auth.getName();
        String password = auth.getCredentials().toString();

        try {
            AccountVO login = authService.login(new AccountForm(username, password));
            if (login != null) {
                return new UsernamePasswordAuthenticationToken(login, null, Collections.emptyList());
            } else {
                throw new BadCredentialsException("External system authentication failed");
            }
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}
