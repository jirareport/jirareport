package br.com.jiratorio.service.impl;

import br.com.jiratorio.client.AuthClient;
import br.com.jiratorio.domain.form.LoginForm;
import br.com.jiratorio.domain.vo.Account;
import br.com.jiratorio.domain.vo.CurrentUser;
import br.com.jiratorio.service.AuthService;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthClient authClient;

    @Override
    public Account login(final LoginForm loginForm) {
        log.info("Method=login, loginForm={}", loginForm);

        String usrPasswd = String.format("%s:%s", loginForm.getUsername(), loginForm.getPassword());
        String token = String.format("Basic %s", new String(Base64.getEncoder().encode(usrPasswd.getBytes())));

        CurrentUser currentUser = authClient.findCurrentUser(token);

        return Account.builder()
                .username(loginForm.getUsername())
                .token(token)
                .currentUser(currentUser)
                .build();
    }

}
