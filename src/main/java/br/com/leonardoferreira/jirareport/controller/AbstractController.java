package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.vo.Account;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author lferreira
 * @since 12/4/17 1:50 PM
 */
public class AbstractController {

    protected Account currentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Account) {
            return (Account) principal;
        } else {
            return null;
        }
    }

    protected boolean authenticated() {
        return currentUser() != null;
    }

}
