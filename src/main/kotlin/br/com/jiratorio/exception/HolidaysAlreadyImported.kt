package br.com.jiratorio.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class HolidaysAlreadyImported(
    message: String? = null,
    cause: Exception? = null
) : RuntimeException(message, cause) {
    companion object {
        private val serialVersionUID = 1189381143800680941L
    }
}
