package br.com.leonardoferreira.jirareport.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GeoNames {

    private String adminCode1;
    private String lng;
    private String lat;
    private String geonameId;
    private String toponymName;
    private String countryId;
    private String population;
    private String countryCode;
    private String name;
    private String countryName;

    @JsonProperty("adminCodes1")
    private Sigla sigla;

}
