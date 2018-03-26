package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.client.AuthClient;
import br.com.leonardoferreira.jirareport.domain.form.AccountForm;
import br.com.leonardoferreira.jirareport.domain.vo.AccountVO;
import br.com.leonardoferreira.jirareport.domain.vo.CurrentUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author leferreira
 * @since 7/28/17 10:14 AM
 */
@Service
public class AuthService {

    @Autowired
    private AuthClient authClient;

    public AccountVO login(AccountForm accountForm) {
        String token = authClient.login(accountForm);
        if (token == null) {
            return null;
        }

        CurrentUserVO currentUser = authClient.getCurrentUser(token);

        AccountVO accountVO = new AccountVO();
        accountVO.setToken(token);
        accountVO.setCurrentUser(currentUser);

        return accountVO;
    }

}
