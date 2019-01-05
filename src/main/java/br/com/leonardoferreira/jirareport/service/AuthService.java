package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.form.LoginForm;
import br.com.leonardoferreira.jirareport.domain.vo.Account;

public interface AuthService {

    Account login(LoginForm loginForm);

}
