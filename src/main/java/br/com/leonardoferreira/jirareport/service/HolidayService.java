package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import java.util.List;

/**
 * @author s2it_leferreira
 * @since 5/7/18 6:52 PM
 */
public interface HolidayService {

    List<Holiday> findAll();

    void create(Holiday holiday);

    void delete(String id);

    Holiday findById(String id);

    void update(Holiday holiday);
}
