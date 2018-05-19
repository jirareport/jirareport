package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.vo.GeoNamesWrapperVO;

/**
 * @author jfalbo
 */
public interface GeoNamesService {

    GeoNamesWrapperVO findAllStatesOfBrazil();

    GeoNamesWrapperVO findAllCitiesByState(String geonameIdState);

}
