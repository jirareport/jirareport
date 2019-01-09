package br.com.leonardoferreira.jirareport.config;

import br.com.leonardoferreira.jirareport.domain.vo.Account;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional
                .ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(it -> (Account) it.getPrincipal())
                .map(Account::getUsername);
    }

}
