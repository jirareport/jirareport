package br.com.jiratorio.domain.request

import javax.validation.constraints.NotBlank

data class LeadTimeConfigRequest(
    @field:NotBlank
    var name: String? = null,

    @field:NotBlank
    var startColumn: String? = null,

    @field:NotBlank
    var endColumn: String? = null
)
