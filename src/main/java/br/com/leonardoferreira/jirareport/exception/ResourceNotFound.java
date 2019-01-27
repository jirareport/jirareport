package br.com.leonardoferreira.jirareport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource Not Found")
public class ResourceNotFound extends RuntimeException {
    private static final long serialVersionUID = 2093334930630431201L;

}
