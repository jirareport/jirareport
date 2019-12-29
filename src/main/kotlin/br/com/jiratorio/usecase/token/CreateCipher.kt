package br.com.jiratorio.usecase.token

import br.com.jiratorio.config.property.SecurityProperties
import br.com.jiratorio.config.stereotype.UseCase
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@UseCase
class CreateCipher(
    private val securityProperties: SecurityProperties
) {

    companion object {
        private const val algorithm = "Blowfish"
    }

    fun execute(mode: Mode): Cipher =
        Cipher.getInstance(algorithm)
            .apply {
                init(
                    mode.intValue,
                    SecretKeySpec(securityProperties.key.toByteArray(Charsets.UTF_8), algorithm)
                )
            }

    enum class Mode(
        val intValue: Int
    ) {
        ENCRYPT(Cipher.ENCRYPT_MODE),
        DECRYPT(Cipher.DECRYPT_MODE)
    }

}
