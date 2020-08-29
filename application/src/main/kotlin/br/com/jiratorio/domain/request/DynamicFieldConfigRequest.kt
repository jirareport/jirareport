package br.com.jiratorio.domain.request

import javax.validation.constraints.NotBlank

data class DynamicFieldConfigRequest(

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val field: String

)
