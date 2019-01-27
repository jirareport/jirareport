package br.com.leonardoferreira.jirareport.service;

import java.time.LocalDate;
import java.util.List;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HolidayService {

    Page<Holiday> findByBoard(Long boardId, Pageable pageable);

    List<Holiday> findByBoard(Long boardId);

    List<LocalDate> findDaysByBoard(Long boardId);

    Long create(Long boardId, Holiday holiday);

    void delete(Long id);

    Holiday findById(Long id);

    void update(Long boardId, Holiday holiday);

    boolean createImported(Long boardId);

}
