package br.com.jiratorio.exception

import java.lang.RuntimeException

class BadCredentialsException(message: String): RuntimeException(message)
