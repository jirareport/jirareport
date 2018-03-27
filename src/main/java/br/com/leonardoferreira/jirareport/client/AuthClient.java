package br.com.leonardoferreira.jirareport.client;

import br.com.leonardoferreira.jirareport.domain.form.AccountForm;
import br.com.leonardoferreira.jirareport.domain.vo.CurrentUserVO;

/**
 * Created by lferreira on 3/26/18
 */
public interface AuthClient {

    String login(AccountForm accountForm);

    CurrentUserVO findCurrentUser(String token);

}
