package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.domain.vo.Account;
import org.springframework.security.core.context.SecurityContextHolder;

public class AbstractService {

    protected Account currentUser() {
        return (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    protected String currentToken() {
        return currentUser().getToken();
    }

}
