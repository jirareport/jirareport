package br.com.jiratorio.domain.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class HolidayRequest {

    @NotBlank
    @Pattern(regexp = "[0-9]{0,2}/[0-9]{0,2}/[0-9]{0,4}")
    private String date;

    @NotBlank
    private String description;

}
