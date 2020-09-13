package br.com.jiratorio.security

import br.com.jiratorio.domain.request.LoginRequest
import br.com.jiratorio.service.AuthService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JiraAuthenticationProvider(
    private val authService: AuthService,
) : AuthenticationProvider {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun authenticate(auth: Authentication): Authentication {
        try {
            val account = authService.login(LoginRequest(auth.name, auth.credentials.toString()))
            val token = authService.encode(account)

            return UsernamePasswordAuthenticationToken(account, account.password, account.authorities)
                .also { it.details = token }
        } catch (e: Exception) {
            log.error("Method=authenticate, E={}", e.message, e)
            throw BadCredentialsException(e.message, e)
        }
    }

    override fun supports(auth: Class<*>): Boolean =
        auth == UsernamePasswordAuthenticationToken::class.java
}
