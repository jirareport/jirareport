package br.com.jiratorio.client.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class HolidayClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.query("json", "true");
        };
    }

}
