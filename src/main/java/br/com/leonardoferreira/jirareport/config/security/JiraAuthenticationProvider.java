package br.com.leonardoferreira.jirareport.config.security;

import br.com.leonardoferreira.jirareport.domain.form.LoginForm;
import br.com.leonardoferreira.jirareport.domain.vo.Account;
import br.com.leonardoferreira.jirareport.service.AuthService;
import br.com.leonardoferreira.jirareport.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JiraAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @Override
    public Authentication authenticate(final Authentication auth) {
        try {
            Account account = authService.login(new LoginForm(auth.getName(), auth.getCredentials().toString()));
            if (account == null) {
                throw new BadCredentialsException("External system authentication failed");
            }

            String token = tokenService.encode(account);

            UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(
                    account, account.getPassword(), account.getAuthorities());
            user.setDetails(token);

            return user;
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
