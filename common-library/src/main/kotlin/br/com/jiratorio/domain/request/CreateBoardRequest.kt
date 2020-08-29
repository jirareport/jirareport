package br.com.jiratorio.domain.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateBoardRequest(

    @field:NotBlank
    val name: String,

    @field:NotNull
    @field:Min(1)
    val externalId: Long

)
