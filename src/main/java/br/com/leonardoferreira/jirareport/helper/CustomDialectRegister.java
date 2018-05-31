package br.com.leonardoferreira.jirareport.helper;

import java.util.Collections;
import java.util.Set;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

@Component
public class CustomDialectRegister implements IDialect, IExpressionObjectDialect {

    @Getter
    private final String name;

    public CustomDialectRegister() {
        this.name = "CustomDialectRegister";
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory() {

            @Override
            public Set<String> getAllExpressionObjectNames() {
                return Collections.singleton("applicationHelper");
            }

            @Override
            public Object buildObject(final IExpressionContext context,
                                      final String expressionObjectName) {
                return new ApplicationHelper();
            }

            @Override
            public boolean isCacheable(final String expressionObjectName) {
                return true;
            }
        };
    }


}