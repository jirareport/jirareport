package br.com.leonardoferreira.jirareport.repository;

import br.com.leonardoferreira.jirareport.domain.Holiday;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lferreira
 * @since 5/7/18 6:51 PM
 */
@Repository
public interface HolidayRepository extends CrudRepository<Holiday, Long> {

    List<Holiday> findAllByBoardId(Long id);

    Page<Holiday> findAllByBoardId(Long id, Pageable pageable);

}
