package br.com.jiratorio.service.auth

import br.com.jiratorio.domain.Account

interface TokenService {

    fun encode(account: Account): String

    fun decode(token: String): Account

}
