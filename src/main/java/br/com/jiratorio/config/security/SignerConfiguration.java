package br.com.jiratorio.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.jwt.crypto.sign.MacSigner;

@Configuration
public class SignerConfiguration {

    @Bean
    public MacSigner signer(@Value("security.key") final String key) {
        return new MacSigner(key);
    }

}
