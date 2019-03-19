package br.com.jiratorio.domain.request

import lombok.Data
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Data
class CreateBoardRequest {

    @NotBlank
    var name: String? = null

    @NotNull
    var externalId: Long? = null

}
