package br.com.leonardoferreira.jirareport.base;

import br.com.leonardoferreira.jirareport.domain.vo.Account;
import br.com.leonardoferreira.jirareport.factory.AccountFactory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected CleanDatabase cleanDatabase;

    @Autowired
    protected AccountFactory accountFactory;

    @BeforeEach
    public void beforeEach() {
        cleanDatabase.clean();
    }

    protected RequestPostProcessor defaultUser() {
        return SecurityMockMvcRequestPostProcessors.user(accountFactory.defaultUser());
    }

    protected void withUser(final String username, final Runnable consumer) {
        SecurityContext oldContext = TestSecurityContextHolder.getContext();

        TestSecurityContextHolder.clearContext();
        Account principal = accountFactory.buildUser(username);
        TestSecurityContextHolder.setAuthentication(new UsernamePasswordAuthenticationToken(principal,
                principal.getPassword(), principal.getAuthorities()));

        consumer.run();

        TestSecurityContextHolder.clearContext();
        TestSecurityContextHolder.setContext(oldContext);
    }
}
