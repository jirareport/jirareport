package br.com.jiratorio.domain.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateBoardRequest(
    @field:NotBlank
    var name: String,

    @field:NotNull
    @field:Min(1)
    var externalId: Long
)
