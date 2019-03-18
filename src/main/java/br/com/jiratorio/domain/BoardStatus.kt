package br.com.jiratorio.domain

data class BoardStatus(
        val self: String,
        val description: String,
        val iconUrl: String,
        val name: String,
        val id: String,
        val statusCategory: StatusCategory
)
