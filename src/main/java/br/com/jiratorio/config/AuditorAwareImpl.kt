package br.com.jiratorio.config

import br.com.jiratorio.domain.Account
import java.util.Optional
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuditorAwareImpl : AuditorAware<String> {

    override fun getCurrentAuditor() =
        Optional.ofNullable(SecurityContextHolder.getContext().authentication)
            .map { it.principal as Account }
            .map { it.username }!!

}
