package br.com.jiratorio.config.security

import br.com.jiratorio.config.properties.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.jwt.crypto.sign.MacSigner

@Configuration
class SignerConfiguration {

    @Bean
    fun signer(securityProperties: SecurityProperties) =
        MacSigner(securityProperties.key)

}
