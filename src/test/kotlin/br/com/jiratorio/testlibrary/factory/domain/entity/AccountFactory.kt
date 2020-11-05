package br.com.jiratorio.testlibrary.factory.domain.entity

import br.com.jiratorio.domain.Account
import org.springframework.stereotype.Component

@Component
class AccountFactory {

    fun defaultUser(): Account {
        return buildUser(defaultUserName())
    }

    fun buildUser(username: String): Account {
        return Account(
            username = username,
            name = "name",
            email = "email@company.com",
            token = "secret-token"
        )
    }

    fun defaultUserName(): String {
        return "default_user"
    }

}
