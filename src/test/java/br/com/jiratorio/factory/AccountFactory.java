package br.com.jiratorio.factory;

import br.com.jiratorio.domain.vo.Account;
import br.com.jiratorio.domain.vo.CurrentUser;
import br.com.jiratorio.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountFactory {

    public static final String DEFAULT_USER = "default_user";

    @Autowired
    private TokenService tokenService;

    public Account defaultUser() {
        return buildUser(DEFAULT_USER);
    }

    public Account buildUser(final String username) {
        return new Account(username, "secret-token",
                new CurrentUser("name", "email@company.com"));
    }

    public String defaultUserToken() {
        return tokenService.encode(defaultUser());
    }
}
