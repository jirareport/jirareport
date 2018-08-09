package br.com.leonardoferreira.jirareport.helper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

@Component
public class CustomDialectRegister implements IDialect, IExpressionObjectDialect, IExpressionObjectFactory {

    @Autowired
    private List<Helper> helpers;

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return this;
    }

    @Override
    public Set<String> getAllExpressionObjectNames() {
        return helpers.stream()
                .map(Helper::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public Object buildObject(final IExpressionContext context,
                              final String expressionObjectName) {
        return helpers.stream()
                .filter(h -> h.getName().equals(expressionObjectName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean isCacheable(final String expressionObjectName) {
        return helpers.stream()
                .filter(h -> h.getName().equals(expressionObjectName))
                .findFirst()
                .map(Helper::isCacheable)
                .orElse(false);
    }

    @Override
    public String getName() {
        return "CustomDialectRegister";
    }
}