package br.com.leonardoferreira.jirareport.repository;

import br.com.leonardoferreira.jirareport.domain.IssuePeriod;
import br.com.leonardoferreira.jirareport.domain.embedded.IssuePeriodId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author lferreira
 * @since 11/21/17 4:34 PM
 */
public interface IssuePeriodRepository extends CrudRepository<IssuePeriod, IssuePeriodId> {

    List<IssuePeriod> findByFormProjectId(Long projectId);

}
