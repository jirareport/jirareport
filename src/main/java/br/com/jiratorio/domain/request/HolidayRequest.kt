package br.com.jiratorio.domain.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class HolidayRequest(

    @field:NotBlank
    @field:Pattern(regexp = "[0-9]{0,2}/[0-9]{0,2}/[0-9]{0,4}")
    var date: String? = null,

    @field:NotBlank
    var description: String? = null

)
