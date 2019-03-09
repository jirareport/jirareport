package br.com.jiratorio.base;

import br.com.jiratorio.domain.Account;
import br.com.jiratorio.factory.entity.AccountFactory;
import io.restassured.http.Header;
import java.util.function.Supplier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Authenticator {

    private final AccountFactory accountFactory;

    public Authenticator(final AccountFactory accountFactory) {
        this.accountFactory = accountFactory;
    }

    public void doWithDefaultUser(final Runnable runnable) {
        doWithUser(AccountFactory.DEFAULT_USER, runnable);
    }

    public void doWithUser(final String username, final Runnable runnable) {
        withUser(username, () -> {
            runnable.run();
            return null;
        });
    }

    public <T> T withDefaultUser(final Supplier<T> supplier) {
        return withUser(AccountFactory.DEFAULT_USER, supplier);
    }

    public <T> T withUser(final String username, final Supplier<T> supplier) {
        SecurityContext oldContext = TestSecurityContextHolder.getContext();

        TestSecurityContextHolder.clearContext();
        Account principal = accountFactory.buildUser(username);
        TestSecurityContextHolder.setAuthentication(new UsernamePasswordAuthenticationToken(principal,
                principal.getPassword(), principal.getAuthorities()));

        T result = supplier.get();

        TestSecurityContextHolder.clearContext();
        TestSecurityContextHolder.setContext(oldContext);

        return result;
    }

    public Header defaultUserHeader() {
        return new Header("X-Auth-Token", accountFactory.defaultUserToken());
    }

}
