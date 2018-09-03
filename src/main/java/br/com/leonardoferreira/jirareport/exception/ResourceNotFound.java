package br.com.leonardoferreira.jirareport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by lferreira on 3/26/18
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException {
    private static final long serialVersionUID = 2093334930630431201L;

}
