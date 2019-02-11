package br.com.jiratorio.service;

import br.com.jiratorio.domain.vo.Account;

public interface TokenService {

    String encode(Account account);

    Account decode(String token);

}
