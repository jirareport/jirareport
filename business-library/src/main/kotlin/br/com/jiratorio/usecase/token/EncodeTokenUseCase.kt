package br.com.jiratorio.usecase.token

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.Account
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.Base64

@UseCase
class EncodeTokenUseCase(
    private val objectMapper: ObjectMapper,
    private val createCipher: CreateCipherUseCase
) {

    fun execute(account: Account): String {
        val cipher = createCipher.execute(CreateCipherUseCase.Mode.ENCRYPT)

        return Base64.getEncoder()
            .encodeToString(
                cipher.doFinal(
                    objectMapper.writeValueAsString(account)
                        .toByteArray(Charsets.UTF_8)
                )
            )
    }

}
