package br.com.jiratorio.config;

import com.github.javafaker.Faker;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FakerConfig {

    @Bean
    public Faker faker() {
        long seed = System.currentTimeMillis();
        log.info("I=generated seed, seed={}", seed);
        return new Faker(new Random(seed));
    }

}
