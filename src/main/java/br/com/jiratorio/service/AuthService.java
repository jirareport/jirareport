package br.com.jiratorio.service;

import br.com.jiratorio.domain.form.LoginForm;
import br.com.jiratorio.domain.vo.Account;

public interface AuthService {

    Account login(LoginForm loginForm);

}
