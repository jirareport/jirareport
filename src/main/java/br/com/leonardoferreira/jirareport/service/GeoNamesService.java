package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.vo.GeoNamesWrapperVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author jfalbo
 */
public interface GeoNamesService {

    GeoNamesWrapperVO findAllStatesOfBrazil();

    GeoNamesWrapperVO findAllCitiesByState(String geonameIdState);

}
