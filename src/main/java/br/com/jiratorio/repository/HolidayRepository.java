package br.com.jiratorio.repository;

import br.com.jiratorio.domain.Holiday;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends CrudRepository<Holiday, Long> {

    List<Holiday> findAllByBoardId(Long id);

    Page<Holiday> findAllByBoardId(Long id, Pageable pageable);

}
