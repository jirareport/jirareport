package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import br.com.leonardoferreira.jirareport.domain.vo.GeoNamesWrapperVO;
import br.com.leonardoferreira.jirareport.domain.vo.HolidayVO;

import java.util.List;

/**
 * @author s2it_leferreira
 * @since 5/7/18 6:52 PM
 */
public interface HolidayService {

    List<Holiday> findByProject(Long projectId);

    void create(Long projectId, Holiday holiday);

    void delete(Long id);

    Holiday findById(Long id);

    void update(Long projectId, Holiday holiday);

    List<HolidayVO> findAllHolidaysInCity(String year, String state, String city);

    Boolean createImported(Long projectId, String city);

    GeoNamesWrapperVO findAllStatesOfBrazil();

    GeoNamesWrapperVO findAllCitiesByState(String geonameIdState);
}
