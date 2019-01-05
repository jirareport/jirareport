package br.com.leonardoferreira.jirareport.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class DefaultAcceptClient implements RequestInterceptor {

    @Override
    public void apply(final RequestTemplate template) {
        if (template != null) {
            template.header("Accept", "application/json");
        }
    }

}
