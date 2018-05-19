package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.form.LoginForm;
import br.com.leonardoferreira.jirareport.domain.vo.Account;

/**
 * Created by lferreira on 3/26/18
 */
public interface AuthService {

    Account login(LoginForm loginForm);

}
