package br.com.leonardoferreira.jirareport.service;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author lferreira
 * @since 5/7/18 6:52 PM
 */
public interface HolidayService {

    Page<Holiday> findByProject(Long projectId, Pageable pageable);

    List<Holiday> findByProject(Long id);

    void create(Long projectId, Holiday holiday);

    void delete(Long id);

    Holiday findById(Long id);

    void update(Long projectId, Holiday holiday);

    boolean createImported(Long projectId, String city);

}
