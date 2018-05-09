package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.domain.vo.Account;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author lferreira
 * @since 11/14/17 3:53 PM
 */
public abstract class AbstractService {

    protected Account currentUser() {
        return (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    protected String currentToken() {
        return currentUser().getToken();
    }

}
