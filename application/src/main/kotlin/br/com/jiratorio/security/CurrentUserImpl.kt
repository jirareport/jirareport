package br.com.jiratorio.security

import br.com.jiratorio.domain.CurrentUser
import br.com.jiratorio.extension.account
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class CurrentUserImpl : CurrentUser {

    override val username: String
        get() = SecurityContextHolder.getContext().account?.username ?: throw IllegalStateException("current user not found")

    override val jiraToken: String
        get() = SecurityContextHolder.getContext().account?.token ?: throw IllegalStateException("current user not found")

}
