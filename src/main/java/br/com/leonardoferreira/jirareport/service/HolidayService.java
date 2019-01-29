package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.request.HolidayRequest;
import br.com.leonardoferreira.jirareport.domain.response.HolidayResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HolidayService {

    Page<HolidayResponse> findByBoard(Long boardId, Pageable pageable);

    List<LocalDate> findDaysByBoard(Long boardId);

    Long create(Long boardId, HolidayRequest holidayRequest);

    void delete(Long id);

    HolidayResponse findById(Long id);

    void update(Long boardId, Long holidayId, HolidayRequest holidayRequest);

    void createImported(Long boardId);

}
