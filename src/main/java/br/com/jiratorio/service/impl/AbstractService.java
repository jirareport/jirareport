package br.com.jiratorio.service.impl;

import br.com.jiratorio.domain.vo.Account;
import org.springframework.security.core.context.SecurityContextHolder;

public class AbstractService {

    protected Account currentUser() {
        return (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    protected String currentToken() {
        return currentUser().getToken();
    }

}
