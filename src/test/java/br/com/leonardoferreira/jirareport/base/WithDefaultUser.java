package br.com.leonardoferreira.jirareport.base;

import br.com.leonardoferreira.jirareport.factory.AccountFactory;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithUserDetails;

@Retention(RetentionPolicy.RUNTIME)
@WithUserDetails(AccountFactory.DEFAULT_USER)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface WithDefaultUser {
}
