package br.com.jiratorio.domain.response

data class LoginResponse(
    val name: String,
    val email: String,
    val token: String,
)
