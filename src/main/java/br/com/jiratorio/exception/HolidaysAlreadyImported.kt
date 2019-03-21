package br.com.jiratorio.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Holidays already imported")
class HolidaysAlreadyImported(e: Exception? = null) : RuntimeException(e) {
    companion object {
        private val serialVersionUID = 1189381143800680941L
    }
}
