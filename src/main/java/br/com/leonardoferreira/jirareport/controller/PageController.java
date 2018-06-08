package br.com.leonardoferreira.jirareport.controller;

import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lferreira
 * @since 11/14/17 3:43 PM
 */
@Slf4j
@Controller
public class PageController extends AbstractController implements ErrorController {

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/")
    public String index() {
        if (authenticated()) {
            return "redirect:/boards";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/error")
    public ModelAndView error(final HttpServletRequest request) {
        String token = UUID.randomUUID().toString();
        log.info("Method=error, MSG=Error trace, Trace={}", token);

        return new ModelAndView("errors/default")
                .addObject("trace", token);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
