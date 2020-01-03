package br.com.jiratorio.domain

import java.time.LocalDateTime

data class FieldChangelog(

    val from: String? = null,

    val to: String? = null,

    val field: String,

    var created: LocalDateTime

)
