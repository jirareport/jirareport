package br.com.jiratorio.service.auth.impl

import br.com.jiratorio.domain.Account
import br.com.jiratorio.service.auth.TokenService
import br.com.jiratorio.usecase.token.DecodeToken
import br.com.jiratorio.usecase.token.EncodeToken
import org.springframework.stereotype.Service

@Service
class TokenServiceImpl(
    private val encodeToken: EncodeToken,
    private val decodeToken: DecodeToken
) : TokenService {

    override fun encode(account: Account): String =
        encodeToken.execute(account)

    override fun decode(token: String): Account =
        decodeToken.execute(token)

}
