package br.com.jiratorio.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
class ResourceNotFound : RuntimeException() {
    companion object {
        private val serialVersionUID = 2093334930630431201L
    }
}
