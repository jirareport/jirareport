package br.com.jiratorio.service

import br.com.jiratorio.domain.request.LoginRequest
import br.com.jiratorio.domain.Account

interface AuthService {

    fun login(loginRequest: LoginRequest): Account

}
