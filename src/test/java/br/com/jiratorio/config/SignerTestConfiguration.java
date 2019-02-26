package br.com.jiratorio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.crypto.sign.MacSigner;

@Configuration
public class SignerTestConfiguration {

    @Bean
    public MacSigner macSigner() {
        return new MacSigner("secret-token-test");
    }

}
