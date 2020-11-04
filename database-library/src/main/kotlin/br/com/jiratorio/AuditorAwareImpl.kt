package br.com.jiratorio

import br.com.jiratorio.domain.CurrentUser
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import java.util.Optional

@Component
internal class AuditorAwareImpl(
    private val currentUser: CurrentUser
) : AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> =
        Optional.ofNullable(currentUser.username)

}
