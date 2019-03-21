package br.com.jiratorio.domain.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateBoardRequest(
    @field:NotBlank
    var name: String? = null,

    @field:NotNull
    var externalId: Long? = null
)
