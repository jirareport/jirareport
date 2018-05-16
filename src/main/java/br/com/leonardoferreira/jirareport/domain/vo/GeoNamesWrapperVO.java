package br.com.leonardoferreira.jirareport.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class GeoNamesWrapperVO {

    private String totalResultsCount;
    private List<GeoNames> geonames;
    
}
