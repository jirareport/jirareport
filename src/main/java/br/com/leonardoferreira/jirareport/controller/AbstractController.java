package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.vo.AccountVO;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author lferreira
 * @since 12/4/17 1:50 PM
 */
public class AbstractController {

    protected AccountVO currentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AccountVO) {
            return (AccountVO) principal;
        } else {
            return null;
        }
    }

    protected boolean authenticated() {
        return currentUser() != null;
    }

}
