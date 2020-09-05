package br.com.jiratorio.security

import br.com.jiratorio.usecase.token.DecodeTokenUseCase
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException
import org.springframework.stereotype.Component

@Component
class TokenAuthenticationProvider(
    private val decodeToken: DecodeTokenUseCase
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        try {
            val account = decodeToken.execute(authentication.principal as String)
            return PreAuthenticatedAuthenticationToken(account, account.password, account.authorities)
        } catch (e: Exception) {
            throw PreAuthenticatedCredentialsNotFoundException(e.message, e)
        }
    }

    override fun supports(authentication: Class<*>): Boolean =
        authentication == PreAuthenticatedAuthenticationToken::class.java

}
