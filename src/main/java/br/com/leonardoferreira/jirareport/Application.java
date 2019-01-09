package br.com.leonardoferreira.jirareport;

import brave.sampler.Sampler;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@EnableCaching
@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
@EnableAspectJAutoProxy
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Executor executor() {
        return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(10));
    }

    @Bean
    public Function<String, String> currentUrlWithoutParam() {
        return param -> ServletUriComponentsBuilder
                .fromCurrentRequest().replaceQueryParam(param).toUriString();
    }

    @Bean
    public Sampler sampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

}
