package br.com.jiratorio.domain.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateBoardRequest(

    @field:NotBlank
    val name: String,

    @field:NotNull
    @field:Min(1)
    val externalId: Long

)
