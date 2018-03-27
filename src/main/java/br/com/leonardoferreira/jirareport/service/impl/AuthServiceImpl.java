package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.client.AuthClient;
import br.com.leonardoferreira.jirareport.domain.form.AccountForm;
import br.com.leonardoferreira.jirareport.domain.vo.AccountVO;
import br.com.leonardoferreira.jirareport.domain.vo.CurrentUserVO;
import br.com.leonardoferreira.jirareport.service.AuthService;
import org.springframework.stereotype.Service;

/**
 * @author lferreira
 * @since 7/28/17 10:14 AM
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthClient authClient;

    public AuthServiceImpl(AuthClient authClient) {
        this.authClient = authClient;
    }

    @Override
    public AccountVO login(AccountForm accountForm) {
        String token = authClient.login(accountForm);
        if (token == null) {
            return null;
        }

        CurrentUserVO currentUser = authClient.findCurrentUser(token);

        AccountVO accountVO = new AccountVO();
        accountVO.setToken(token);
        accountVO.setCurrentUser(currentUser);

        return accountVO;
    }

}
