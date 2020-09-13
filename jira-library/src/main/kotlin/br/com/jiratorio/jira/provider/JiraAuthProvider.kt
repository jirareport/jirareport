package br.com.jiratorio.jira.provider

import br.com.jiratorio.domain.request.LoginRequest
import br.com.jiratorio.domain.response.LoginResponse
import br.com.jiratorio.extension.toBase64
import br.com.jiratorio.jira.client.AuthClient
import br.com.jiratorio.provider.AuthProvider
import org.springframework.stereotype.Component

@Component
class JiraAuthProvider(
    private val authClient: AuthClient,
) : AuthProvider {

    override fun login(loginRequest: LoginRequest): LoginResponse {
        val (username, password) = loginRequest

        val token =
            """Basic ${"$username:$password".toBase64()}"""

        val (name, email) = authClient.login(token)

        return LoginResponse(
            name = name,
            email = email,
            token = token
        )
    }

}
