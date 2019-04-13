package br.com.jiratorio.config.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.jwt.crypto.sign.MacSigner

@Configuration
class SignerConfiguration {

    @Bean
    fun signer(@Value("security.key") key: String) =
        MacSigner(key)

}
