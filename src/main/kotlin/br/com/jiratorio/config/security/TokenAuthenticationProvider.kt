package br.com.jiratorio.config.security

import br.com.jiratorio.service.auth.TokenService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException
import org.springframework.stereotype.Component

@Component
class TokenAuthenticationProvider(private val tokenService: TokenService) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        try {
            val account = tokenService.decode(authentication.principal as String)
            return PreAuthenticatedAuthenticationToken(account, account.password, account.authorities)
        } catch (e: Exception) {
            throw PreAuthenticatedCredentialsNotFoundException(e.message, e)
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == PreAuthenticatedAuthenticationToken::class.java
    }

}
