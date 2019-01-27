package br.com.leonardoferreira.jirareport.service.impl;

import br.com.leonardoferreira.jirareport.domain.vo.Account;
import br.com.leonardoferreira.jirareport.exception.ParseTokenException;
import br.com.leonardoferreira.jirareport.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SignatureVerifier signatureVerifier;

    @Autowired
    private Signer signer;

    @Override
    public String encode(final Account account) {
        log.info("Method=encode, account={}", account);
        try {
            String json = objectMapper.writeValueAsString(account);
            Jwt jwt = JwtHelper.encode(json, signer);
            return jwt.getEncoded();
        } catch (Exception e) {
            throw new ParseTokenException(e);
        }
    }

    @Override
    public Account decode(final String token) {
        log.info("Method=decode, token={}", token);

        try {
            Jwt jwt = JwtHelper.decodeAndVerify(token, signatureVerifier);
            return objectMapper.readValue(jwt.getClaims(), Account.class);
        } catch (Exception e) {
            throw new ParseTokenException(e);
        }
    }
}
