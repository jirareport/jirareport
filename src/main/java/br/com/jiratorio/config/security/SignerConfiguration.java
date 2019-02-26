package br.com.jiratorio.config.security;

import java.io.IOException;
import java.nio.charset.Charset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.util.StreamUtils;

@Configuration
@Profile("!test")
public class SignerConfiguration {

    @Bean
    public Signer signer(final ResourceLoader resourceLoader,
                            @Value("${ssh-key.private}") final String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        return new RsaSigner(
                StreamUtils.copyToString(resource.getInputStream(), Charset.forName("UTF-8")));
    }

    @Bean
    public SignatureVerifier verifier(final ResourceLoader resourceLoader,
                                         @Value("${ssh-key.public}") final String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        return new RsaVerifier(
                StreamUtils.copyToString(resource.getInputStream(), Charset.forName("UTF-8")));
    }

}
