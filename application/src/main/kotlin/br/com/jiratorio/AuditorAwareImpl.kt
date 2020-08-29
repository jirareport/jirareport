package br.com.jiratorio

import br.com.jiratorio.extension.account
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class AuditorAwareImpl : AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> =
        Optional.ofNullable(SecurityContextHolder.getContext().account?.username)

}
