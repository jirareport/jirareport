package br.com.jiratorio.service.impl;

import br.com.jiratorio.client.AuthClient;
import br.com.jiratorio.domain.request.LoginRequest;
import br.com.jiratorio.domain.Account;
import br.com.jiratorio.domain.CurrentUser;
import br.com.jiratorio.service.AuthService;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthClient authClient;

    public AuthServiceImpl(final AuthClient authClient) {
        this.authClient = authClient;
    }

    @Override
    public Account login(final LoginRequest loginRequest) {
        log.info("Method=login, loginRequest={}", loginRequest);

        String usrPasswd = String.format("%s:%s", loginRequest.getUsername(), loginRequest.getPassword());
        String token = String.format("Basic %s", new String(Base64.getEncoder().encode(usrPasswd.getBytes())));

        CurrentUser currentUser = authClient.findCurrentUser(token);

        return Account.builder()
                .username(loginRequest.getUsername())
                .token(token)
                .currentUser(currentUser)
                .build();
    }

}
