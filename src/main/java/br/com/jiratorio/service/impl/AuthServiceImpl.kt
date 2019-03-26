package br.com.jiratorio.service.impl

import br.com.jiratorio.client.AuthClient
import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.request.LoginRequest
import br.com.jiratorio.extension.logger
import br.com.jiratorio.extension.toBase64
import br.com.jiratorio.service.AuthService
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(private val authClient: AuthClient) : AuthService {

    private val log = logger()

    override fun login(loginRequest: LoginRequest): Account {
        log.info("Method=login, loginRequest={}", loginRequest)

        val (username, password) = loginRequest
        val token = """Basic ${"$username:$password".toBase64()}"""

        val (name, email) = authClient.login(token)
        return Account(username, name, email, token)
    }

}
