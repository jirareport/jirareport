package br.com.jiratorio.config.security

import br.com.jiratorio.domain.request.LoginRequest
import br.com.jiratorio.service.AuthService
import br.com.jiratorio.service.TokenService
import br.com.jiratorio.extension.log
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JiraAuthenticationProvider(
    private val authService: AuthService,
    private val tokenService: TokenService
) : AuthenticationProvider {

    override fun authenticate(auth: Authentication): Authentication {
        try {
            val account = authService.login(LoginRequest(auth.name, auth.credentials.toString()))
            val token = tokenService.encode(account)

            return UsernamePasswordAuthenticationToken(account, account.password, account.authorities).also {
                it.details = token
            }
        } catch (e: Exception) {
            log.error("Method=authenticate, E={}", e.message, e)
            throw BadCredentialsException(e.message, e)
        }
    }

    override fun supports(auth: Class<*>): Boolean {
        return auth == UsernamePasswordAuthenticationToken::class.java
    }
}
