package br.com.jiratorio.extension

import br.com.jiratorio.domain.Account
import org.springframework.security.core.context.SecurityContext

inline val SecurityContext.account: Account?
    get() = this.authentication?.principal as? Account
