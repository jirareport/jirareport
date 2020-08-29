package br.com.jiratorio.security

import br.com.jiratorio.domain.CurrentUser
import br.com.jiratorio.extension.account
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.WebApplicationContext
import java.lang.IllegalStateException

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
class CurrentUserImpl : CurrentUser {

    override val jiraToken: String
        get() = SecurityContextHolder.getContext().account?.token ?: throw IllegalStateException("current user not found")

}
