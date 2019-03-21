package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.Account
import br.com.jiratorio.service.TokenService
import br.com.jiratorio.extension.logger
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.jwt.JwtHelper
import org.springframework.security.jwt.crypto.sign.SignatureVerifier
import org.springframework.security.jwt.crypto.sign.Signer
import org.springframework.stereotype.Service

@Service
class TokenServiceImpl(
    private val objectMapper: ObjectMapper,
    private val signatureVerifier: SignatureVerifier,
    private val signer: Signer
) : TokenService {

    private val log = logger()

    override fun encode(account: Account): String {
        log.info("Method=encode, account={}", account)

        val json = objectMapper.writeValueAsString(account)
        return JwtHelper.encode(json, signer).encoded
    }

    override fun decode(token: String): Account {
        log.info("Method=decode, token={}", token)

        val jwt = JwtHelper.decodeAndVerify(token, signatureVerifier)
        return objectMapper.readValue(jwt.claims, Account::class.java)
    }
}
