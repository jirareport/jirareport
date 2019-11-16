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
    securityProperties: SecurityProperties
) : TokenService {

    companion object {
        private const val algorithm = "Blowfish"
    }

    private val encryptCipher: Cipher
    private val decryptCipher: Cipher

    init {
        val key = SecretKeySpec(securityProperties.key.toByteArray(), algorithm)

        encryptCipher = Cipher.getInstance(algorithm)
        encryptCipher.init(Cipher.ENCRYPT_MODE, key)

        decryptCipher = Cipher.getInstance(algorithm)
        decryptCipher.init(Cipher.DECRYPT_MODE, key)
    }

    override fun encode(account: Account): String =
        encryptCipher.doFinal(account.toJson())
            .toBase64()

    override fun decode(token: String): Account =
        decryptCipher.doFinal(token.fromBase64())
            .fromJson()

    private fun ByteArray.toBase64(): String =
        Base64.getEncoder()
            .encodeToString(this)

    private fun String.fromBase64(): ByteArray =
        Base64.getDecoder()
            .decode(this)

    private inline fun <reified T> ByteArray.fromJson(): T =
        objectMapper.readValue(this)

    private fun Account.toJson(): ByteArray? =
        objectMapper.writeValueAsBytes(this)

}
