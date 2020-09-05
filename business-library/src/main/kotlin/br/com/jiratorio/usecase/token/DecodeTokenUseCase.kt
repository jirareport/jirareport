package br.com.jiratorio.usecase.token

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.Account
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.Base64

@UseCase
class DecodeTokenUseCase(
    private val objectMapper: ObjectMapper,
    private val createCipher: CreateCipherUseCase
) {

    fun execute(token: String): Account {
        val decryptCipher = createCipher.execute(CreateCipherUseCase.Mode.DECRYPT)

        return objectMapper.readValue(
            String(
                decryptCipher.doFinal(Base64.getDecoder().decode(token)),
                Charsets.UTF_8
            )
        )
    }

}
