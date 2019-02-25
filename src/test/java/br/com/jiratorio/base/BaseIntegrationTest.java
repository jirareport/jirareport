package br.com.jiratorio.base;

import br.com.jiratorio.domain.vo.Account;
import br.com.jiratorio.factory.entity.AccountFactory;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.Header;
import io.restassured.specification.ResponseSpecification;
import java.util.function.Supplier;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public class BaseIntegrationTest {

    @Autowired
    protected CleanDatabase cleanDatabase;

    @Autowired
    protected AccountFactory accountFactory;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    public void beforeEach() {
        RestAssured.port = port;
        cleanDatabase.clean();
    }

    protected RequestPostProcessor defaultUser() {
        return SecurityMockMvcRequestPostProcessors.user(accountFactory.defaultUser());
    }

    protected void withDefaultUser(final Runnable runnable) {
        withUser(AccountFactory.DEFAULT_USER, runnable);
    }

    protected <T> T withDefaultUser(final Supplier<T> supplier) {
        return withUser(AccountFactory.DEFAULT_USER, supplier);
    }

    protected <T> T withUser(final String username, final Supplier<T> supplier) {
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

    protected void withUser(final String username, final Runnable runnable) {
        withUser(username, () -> {
            runnable.run();
            return null;
        });
    }

    protected Header defaultUserHeader() {
        return new Header("X-Auth-Token", accountFactory.defaultUserToken());
    }

    protected ResponseSpecification notFoundSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(HttpStatus.SC_NOT_FOUND)
                .expectBody("error", Matchers.is("Not Found"))
                .expectBody("message", Matchers.is("Resource Not Found"))
                .expectBody("status", Matchers.is(404))
                .build();
    }
}
