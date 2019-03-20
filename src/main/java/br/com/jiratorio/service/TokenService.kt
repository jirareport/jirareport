package br.com.jiratorio.service

import br.com.jiratorio.domain.Account

interface TokenService {

    fun encode(account: Account): String

    fun decode(token: String): Account

}
