package br.com.leonardoferreira.jirareport.domain.form;

import br.com.leonardoferreira.jirareport.util.DateUtil;
import br.com.leonardoferreira.jirareport.util.StringUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserConfigForm {

    @NotBlank(message = "Estado não pode ser vazio.")
    private String state;

    @NotBlank(message = "Cidade não pode ser vazia.")
    private String city;

    @NotBlank(message = "Token não pode ser vazio.")
    private String holidayToken;

    public String getCity() {
        if (this.city == null) {
            return null;
        }

        return StringUtil.stripAccents(this.city)
                .replaceAll(" ", "_")
                .toUpperCase(DateUtil.LOCALE_BR);
    }

}
