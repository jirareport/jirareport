package br.com.jiratorio.domain.request

data class LoginRequest(
    val username: String,
    val password: String
) {

    override fun toString(): String {
        return "LoginRequest(username='$username')"
    }

}
