package br.com.leonardoferreira.jirareport.repository;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import org.springframework.data.repository.CrudRepository;

/**
 * @author s2it_leferreira
 * @since 5/7/18 6:51 PM
 */
public interface HolidayRepository extends CrudRepository<Holiday, Long> {

}
