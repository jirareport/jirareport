package br.com.leonardoferreira.jirareport.config;

import br.com.leonardoferreira.jirareport.exception.ResourceNotFound;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ResourceNotFound.class)
    public ModelAndView resourceNotFoundHander(final ResourceNotFound resourceNotFound) {
        resourceNotFound.printStackTrace();
        return new ModelAndView("errors/resourceNotFound")
                .addObject("exception", resourceNotFound);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView genericHandler(final Exception e) {
        e.printStackTrace();
        return new ModelAndView("errors/generic")
                .addObject("exception", e);
    }

}
