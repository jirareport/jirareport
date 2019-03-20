package br.com.jiratorio.domain

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

data class JiraError(
    val message: String,

    @field:JsonProperty("status-code")
    val statusCode: Long,

    var errorMessages: List<String>? = null
)
