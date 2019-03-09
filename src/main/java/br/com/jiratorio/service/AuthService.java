package br.com.jiratorio.service;

import br.com.jiratorio.domain.request.LoginRequest;
import br.com.jiratorio.domain.Account;

public interface AuthService {

    Account login(LoginRequest loginRequest);

}
