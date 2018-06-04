package br.com.leonardoferreira.jirareport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lferreira
 * @since 11/14/17 3:43 PM
 */
@Controller
public class PageController extends AbstractController {

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/")
    public String index() {
        if (authenticated()) {
            return "redirect:/boards";
        } else {
            return "home";
        }
    }

}
