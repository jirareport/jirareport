package br.com.jiratorio.domain.request

data class SearchBoardRequest(
    val name: String? = null,
    val owner: String? = null
)
