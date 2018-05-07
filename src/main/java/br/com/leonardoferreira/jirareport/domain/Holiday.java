package br.com.leonardoferreira.jirareport.domain;

import br.com.leonardoferreira.jirareport.util.DateUtil;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

/**
 * @author s2it_leferreira
 * @since 5/7/18 6:49 PM
 */
@Data
public class Holiday {

    @Id
    @NotEmpty(message = "A data deve ser informada.")
    @Pattern(regexp = "[0-9]{2}/[0-9]{2}/[0-9]{4}", message = "Deve ser uma data valida.")
    private String date;

    @NotEmpty(message = "A descrição deve ser informada.")
    private String description;

    public String getId() {
        return DateUtil.toENDate(date);
    }

}
