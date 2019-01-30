package br.com.leonardoferreira.jirareport.domain.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LeadTimeConfigRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String startColumn;

    @NotBlank
    private String endColumn;

}
