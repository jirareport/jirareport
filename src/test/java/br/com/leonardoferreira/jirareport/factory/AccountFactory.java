package br.com.leonardoferreira.jirareport.factory;

import br.com.leonardoferreira.jirareport.domain.vo.Account;
import br.com.leonardoferreira.jirareport.domain.vo.CurrentUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AccountFactory implements UserDetailsService {

    public static final String DEFAULT_USER = "default_user";

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return buildUser(username);
    }

    public Account defaultUser() {
        return buildUser(DEFAULT_USER);
    }

    public Account buildUser(final String username) {
        return new Account(username, "secret-token",
                new CurrentUser("name", "email@company.com"));
    }
}
