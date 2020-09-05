package br.com.jiratorio.usecase.auth

import br.com.jiratorio.client.AuthClient
import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.request.LoginRequest
import br.com.jiratorio.extension.toBase64
import br.com.jiratorio.stereotype.UseCase
import org.slf4j.LoggerFactory

@UseCase
class LoginUseCase(private val authClient: AuthClient) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(loginRequest: LoginRequest): Account {
        log.info("Action=login, loginRequest={}", loginRequest)

        val (username, password) = loginRequest
        val token =
            """Basic ${"$username:$password".toBase64()}"""

        val (name, email) = authClient.login(token)
        return Account(username, name, email, token)
    }

}
