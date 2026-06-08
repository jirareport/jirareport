package br.com.jiratorio.domain.request

import jakarta.validation.constraints.NotBlank

data class DynamicFieldConfigRequest(

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val field: String

)
