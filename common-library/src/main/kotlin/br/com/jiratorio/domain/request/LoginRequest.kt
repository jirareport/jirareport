package br.com.jiratorio.domain.request

import br.com.jiratorio.extension.toStringBuilder

data class LoginRequest(

    val username: String,

    val password: String

) {

    override fun toString() =
        toStringBuilder(LoginRequest::username)

}
