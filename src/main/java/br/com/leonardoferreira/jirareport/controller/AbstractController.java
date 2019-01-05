package br.com.leonardoferreira.jirareport.controller;

import br.com.leonardoferreira.jirareport.domain.vo.Account;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    protected void addFlashSuccess(final RedirectAttributes redirectAttributes, final String msg) {
        redirectAttributes.addFlashAttribute("flashSuccess", msg);
    }

    protected void addFlashError(final RedirectAttributes redirectAttributes, final String msg) {
        redirectAttributes.addFlashAttribute("flashError", msg);
    }

}
