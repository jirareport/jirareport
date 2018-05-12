package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import java.util.List;

/**
 * @author s2it_leferreira
 * @since 5/7/18 6:52 PM
 */
public interface HolidayService {

    List<Holiday> findAll();

    List<Holiday> findByProject(Long projectId);

    void create(Long projectId, Holiday holiday);

    void delete(Long id);

    Holiday findById(Long id);

    void update(Long projectId, Holiday holiday);
}
