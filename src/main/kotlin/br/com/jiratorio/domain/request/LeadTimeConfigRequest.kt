package br.com.jiratorio.domain.request

import jakarta.validation.constraints.NotBlank

data class LeadTimeConfigRequest(

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val startColumn: String,

    @field:NotBlank
    val endColumn: String

)
