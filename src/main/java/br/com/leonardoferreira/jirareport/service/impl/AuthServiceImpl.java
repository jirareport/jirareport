package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.client.AuthClient;
import br.com.leonardoferreira.jirareport.domain.form.LoginForm;
import br.com.leonardoferreira.jirareport.domain.vo.Account;
import br.com.leonardoferreira.jirareport.domain.vo.CurrentUser;
import br.com.leonardoferreira.jirareport.domain.vo.SessionInfo;
import br.com.leonardoferreira.jirareport.service.AuthService;
import org.springframework.stereotype.Service;

/**
 * @author lferreira
 * @since 7/28/17 10:14 AM
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthClient authClient;

    public AuthServiceImpl(final AuthClient authClient) {
        this.authClient = authClient;
    }

    @Override
    public Account login(final LoginForm loginForm) {
        SessionInfo sessionInfo = authClient.login(loginForm);

        if (sessionInfo == null || sessionInfo.getSession() == null) {
            return null;
        }

        String token = sessionInfo.getSession().getName() + "=" + sessionInfo.getSession().getValue();

        CurrentUser currentUser = authClient.findCurrentUser(token);

        Account account = new Account();
        account.setToken(token);
        account.setCurrentUser(currentUser);

        return account;
    }

}
