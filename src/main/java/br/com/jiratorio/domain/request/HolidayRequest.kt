package br.com.jiratorio.domain.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

class HolidayRequest {

    @NotBlank
    @Pattern(regexp = "[0-9]{0,2}/[0-9]{0,2}/[0-9]{0,4}")
    var date: String? = null

    @NotBlank
    var description: String? = null

}
