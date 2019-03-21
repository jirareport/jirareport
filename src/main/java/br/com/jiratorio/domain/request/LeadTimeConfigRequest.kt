package br.com.jiratorio.domain.request

import javax.validation.constraints.NotBlank

class LeadTimeConfigRequest {

    @NotBlank
    var name: String? = null

    @NotBlank
    var startColumn: String? = null

    @NotBlank
    var endColumn: String? = null

}
