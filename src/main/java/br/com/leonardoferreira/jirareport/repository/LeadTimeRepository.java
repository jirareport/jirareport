package br.com.leonardoferreira.jirareport.repository;

import br.com.leonardoferreira.jirareport.domain.LeadTime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lferreira on 17/05/18
 */
@Repository
public interface LeadTimeRepository extends CrudRepository<LeadTime, Long> {

    void deleteByIssueId(Long id);

}
