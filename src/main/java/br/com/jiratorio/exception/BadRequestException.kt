package br.com.jiratorio.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException(message: String) : RuntimeException(message) {
    companion object {
        private val serialVersionUID = 2093334930630431201L
    }
}
