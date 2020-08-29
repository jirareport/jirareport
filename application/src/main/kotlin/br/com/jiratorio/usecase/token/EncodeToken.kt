package br.com.jiratorio.usecase.token

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.Account
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.Base64

@UseCase
class EncodeToken(
    private val objectMapper: ObjectMapper,
    private val createCipher: CreateCipher
) {

    fun execute(account: Account): String {
        val cipher = createCipher.execute(CreateCipher.Mode.ENCRYPT)

        return Base64.getEncoder()
            .encodeToString(
                cipher.doFinal(
                    objectMapper.writeValueAsString(account)
                        .toByteArray(Charsets.UTF_8)
                )
            )
    }

}
