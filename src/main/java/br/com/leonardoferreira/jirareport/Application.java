package br.com.leonardoferreira.jirareport;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;

import br.com.leonardoferreira.jirareport.domain.vo.Account;
import brave.sampler.Sampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.format.Formatter;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public AuditorAware<String> auditorAware() {
        return () -> {
            Account principal = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Optional.ofNullable(principal.getUsername());
        };
    }

    @Bean
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<LocalDate>() {
            @Override
            public LocalDate parse(final String text, final Locale locale) throws ParseException {
                return LocalDate.parse(text, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }

            @Override
            public String print(final LocalDate object, final Locale locale) {
                return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(object);
            }
        };
    }

    @Bean
    public Sampler sampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
