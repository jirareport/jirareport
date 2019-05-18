package br.com.jiratorio.service.auth

import br.com.jiratorio.domain.request.LoginRequest
import br.com.jiratorio.domain.Account

interface AuthService {

    fun login(loginRequest: LoginRequest): Account

}
