package br.com.jiratorio.service.auth.impl

import br.com.jiratorio.config.properties.SecurityProperties
import br.com.jiratorio.domain.Account
import br.com.jiratorio.service.auth.TokenService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Service
class TokenServiceImpl(
    private val objectMapper: ObjectMapper,
    private val securityProperties: SecurityProperties
) : TokenService {

    companion object {
        private const val algorithm = "Blowfish"
    }

    override fun encode(account: Account): String {
        val key = SecretKeySpec(securityProperties.key.toByteArray(Charsets.UTF_8), algorithm)

        val encryptCipher = Cipher.getInstance(algorithm)
        encryptCipher.init(Cipher.ENCRYPT_MODE, key)

        return Base64.getEncoder()
            .encodeToString(
                encryptCipher.doFinal(
                    objectMapper.writeValueAsString(account)
                        .toByteArray(Charsets.UTF_8)
                )
            )
    }

    override fun decode(token: String): Account {
        val key = SecretKeySpec(securityProperties.key.toByteArray(Charsets.UTF_8), algorithm)

        val decryptCipher = Cipher.getInstance(algorithm)
        decryptCipher.init(Cipher.DECRYPT_MODE, key)

        return objectMapper.readValue(
            String(
                decryptCipher.doFinal(Base64.getDecoder().decode(token)),
                Charsets.UTF_8
            )
        )
    }

}
