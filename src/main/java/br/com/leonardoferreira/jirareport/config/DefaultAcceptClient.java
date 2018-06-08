package br.com.leonardoferreira.jirareport.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * @author lferreira on 07/06/18
 */
@Component
public class DefaultAcceptClient implements RequestInterceptor {

    @Override
    public void apply(final RequestTemplate template) {
        if (template != null) {
            template.header("Accept", "application/json");
        }
    }

}
