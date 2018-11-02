package br.com.leonardoferreira.jirareport.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DynamicFieldConfig implements Serializable {
    private static final long serialVersionUID = -8677745120309321988L;

    private String name;

    private String field;

}
