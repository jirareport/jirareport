package br.com.jiratorio;

import com.github.javafaker.Faker;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class TestConfig {

    @Bean
    public Faker faker() {
        long seed = System.currentTimeMillis();
        log.info("I=generated seed, seed={}", seed);
        return new Faker(new Random(seed));
    }

    @Bean
    public WireMockServer wireMockServer() {
        WireMockServer wireMockServer = new WireMockServer(
                WireMockConfiguration
                        .options()
                        .port(8888));
        wireMockServer.start();

        return wireMockServer;
    }

}
