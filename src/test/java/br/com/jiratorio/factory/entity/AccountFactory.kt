package br.com.jiratorio.factory.entity

import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.CurrentUser
import br.com.jiratorio.service.TokenService
import org.springframework.stereotype.Component

@Component
class AccountFactory(
        private val tokenService: TokenService
) {

    fun defaultUser() =
            buildUser(defaultUserName())

    fun buildUser(username: String) =
            Account(username, "secret-token", CurrentUser("name", "email@company.com"))

    fun defaultUserToken() =
            tokenService.encode(defaultUser())

    fun defaultUserName() =
            "default_user"

}
