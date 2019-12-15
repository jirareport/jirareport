package br.com.jiratorio.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class MissingBoardConfigurationException(val field: String) : RuntimeException()
