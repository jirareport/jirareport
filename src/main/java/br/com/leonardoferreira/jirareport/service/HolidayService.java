package br.com.leonardoferreira.jirareport.service;

import java.time.LocalDate;
import java.util.List;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author lferreira
 * @since 5/7/18 6:52 PM
 */
public interface HolidayService {

    Page<Holiday> findByBoard(Long boardId, Pageable pageable);

    List<Holiday> findByBoard(Long boardId);

    List<LocalDate> findDaysByBoard(Long boardId);

    void create(Long boardId, Holiday holiday);

    void delete(Long id);

    Holiday findById(Long id);

    void update(Long boardId, Holiday holiday);

    boolean createImported(Long boardId);

}
