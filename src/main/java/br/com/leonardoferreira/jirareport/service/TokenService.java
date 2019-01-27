package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.vo.Account;

public interface TokenService {

    String encode(Account account);

    Account decode(String token);

}
