package br.com.leonardoferreira.jirareport.config.security;

import br.com.leonardoferreira.jirareport.domain.form.LoginForm;
import br.com.leonardoferreira.jirareport.domain.vo.Account;
import br.com.leonardoferreira.jirareport.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JiraAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AuthService authService;

    @Autowired
    private RsaSigner rsaSigner;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Authentication authenticate(final Authentication auth) {
        try {
            Account account = authService.login(new LoginForm(auth.getName(), auth.getCredentials().toString()));
            if (account == null) {
                throw new BadCredentialsException("External system authentication failed");
            }

            Jwt jwt = JwtHelper.encode(objectMapper.writeValueAsString(account), rsaSigner);

            UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(
                    account, account.getPassword(), account.getAuthorities());
            user.setDetails(jwt.getEncoded());

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
