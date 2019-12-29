package br.com.jiratorio.service.auth.impl

import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.request.LoginRequest
import br.com.jiratorio.service.auth.AuthService
import br.com.jiratorio.usecase.auth.Login
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val login: Login
) : AuthService {

    override fun login(loginRequest: LoginRequest): Account =
        login.execute(loginRequest)

}
