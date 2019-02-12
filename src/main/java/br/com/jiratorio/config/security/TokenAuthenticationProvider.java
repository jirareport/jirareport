package br.com.jiratorio.config.security;

import br.com.jiratorio.domain.vo.Account;
import br.com.jiratorio.service.TokenService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final TokenService tokenService;

    public TokenAuthenticationProvider(final TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        try {
            Account account = tokenService.decode((String) authentication.getPrincipal());
            return new PreAuthenticatedAuthenticationToken(account, account.getPassword(), account.getAuthorities());
        } catch (Exception e) {
            throw new PreAuthenticatedCredentialsNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }

}