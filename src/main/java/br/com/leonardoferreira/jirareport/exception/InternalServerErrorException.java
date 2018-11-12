package br.com.leonardoferreira.jirareport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RuntimeException {
    private static final long serialVersionUID = 2093334930630431201L;

    public InternalServerErrorException(final String message) {
        super(message);
    }
}
