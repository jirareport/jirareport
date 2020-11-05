package br.com.jiratorio.testlibrary

import br.com.jiratorio.testlibrary.factory.domain.entity.AccountFactory
import br.com.jiratorio.service.AuthService
import io.restassured.http.Header
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.test.context.TestSecurityContextHolder
import org.springframework.stereotype.Component

@Component
class Authenticator(
    private val accountFactory: AccountFactory,
    private val authService: AuthService,
) {

    fun <T> withDefaultUser(supplier: () -> T): T =
        withUser(accountFactory.defaultUserName(), supplier)

    fun <T> withUser(username: String, supplier: () -> T): T {
        val oldContext = TestSecurityContextHolder.getContext()

        TestSecurityContextHolder.clearContext()
        val principal = accountFactory.buildUser(username)
        TestSecurityContextHolder.setAuthentication(
            UsernamePasswordAuthenticationToken(
                principal,
                principal.password,
                principal.authorities
            )
        )

        val result = supplier()

        TestSecurityContextHolder.clearContext()
        TestSecurityContextHolder.setContext(oldContext)

        return result
    }

    fun defaultUserHeader(): Header =
        Header("X-Auth-Token", authService.encode(accountFactory.defaultUser()))

    fun defaultUserName(): String =
        accountFactory.defaultUserName()
}
