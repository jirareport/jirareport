package br.com.leonardoferreira.jirareport.repository;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lferreira
 * @since 11/21/17 4:34 PM
 */
@Repository
public interface IssuePeriodRepository extends CrudRepository<IssuePeriod, IssuePeriodId> {

    List<IssuePeriod> findByIdProjectId(Long projectId);

}
