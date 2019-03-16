package br.com.jiratorio.domain.request

import lombok.Data
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Data
class CreateBoardRequest {

    @NotBlank
    var name: String? = null

    @NotNull
    var externalId: Long? = null

}
