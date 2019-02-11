package br.com.jiratorio.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicFieldConfig implements Serializable {
    private static final long serialVersionUID = -8677745120309321988L;

    private String name;

    private String field;

}
