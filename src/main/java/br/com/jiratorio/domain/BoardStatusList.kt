package br.com.jiratorio.domain

data class BoardStatusList(
        val self: String,
        val id: String,
        val name: String,
        val subtask: String,
        val statuses: List<BoardStatus>
)
