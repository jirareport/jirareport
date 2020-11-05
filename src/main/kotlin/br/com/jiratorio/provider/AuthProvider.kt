package br.com.jiratorio.provider

import br.com.jiratorio.domain.request.LoginRequest
import br.com.jiratorio.domain.response.LoginResponse

interface AuthProvider {

    fun login(loginRequest: LoginRequest): LoginResponse

}
