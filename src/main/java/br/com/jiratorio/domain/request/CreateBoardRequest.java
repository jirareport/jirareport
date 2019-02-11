package br.com.jiratorio.domain.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBoardRequest {

    @NotBlank
    private String name;

    @NotNull
    private Long externalId;

}
