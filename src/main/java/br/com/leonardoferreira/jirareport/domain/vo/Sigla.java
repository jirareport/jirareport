package br.com.leonardoferreira.jirareport.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Sigla {

    @JsonProperty("ISO3166_2")
    private String descricao;
}
