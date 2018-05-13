package br.com.leonardoferreira.jirareport.service;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.Holiday;

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

    boolean createImported(Long projectId);
}
