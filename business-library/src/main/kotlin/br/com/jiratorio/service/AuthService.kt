package br.com.jiratorio.service

import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.request.LoginRequest
import br.com.jiratorio.property.SecurityProperties
import br.com.jiratorio.provider.AuthProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Service
class AuthService(
    private val authProvider: AuthProvider,
    private val objectMapper: ObjectMapper,
    private val securityProperties: SecurityProperties,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun login(loginRequest: LoginRequest): Account {
        log.info("Action=login, loginRequest={}", loginRequest)

        val (name, email, token) = authProvider.login(loginRequest)

        return Account(
            username = loginRequest.username,
            name = name,
            email = email,
            token = token
        )
    }

    fun decode(token: String): Account {
        val decryptCipher = createCipher(Mode.DECRYPT)

        return objectMapper.readValue(
            String(
                decryptCipher.doFinal(Base64.getDecoder().decode(token)),
                Charsets.UTF_8
            )
        )
    }

    fun encode(account: Account): String {
        val cipher = createCipher(Mode.ENCRYPT)

        return Base64.getEncoder()
            .encodeToString(
                cipher.doFinal(
                    objectMapper.writeValueAsString(account)
                        .toByteArray(Charsets.UTF_8)
                )
            )
    }


    private fun createCipher(mode: Mode): Cipher =
        Cipher.getInstance(algorithm)
            .apply {
                init(
                    mode.intValue,
                    SecretKeySpec(securityProperties.key.toByteArray(Charsets.UTF_8), algorithm)
                )
            }

    private enum class Mode(val intValue: Int) {
        ENCRYPT(Cipher.ENCRYPT_MODE),
        DECRYPT(Cipher.DECRYPT_MODE)
    }

    companion object {
        private const val algorithm = "Blowfish"
    }

}
