package br.com.jiratorio.extension

import br.com.jiratorio.domain.Account
import org.springframework.security.core.context.SecurityContext

fun SecurityContext.account(): Account? {
    return this.authentication?.principal as? Account
}
