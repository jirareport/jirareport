package br.com.leonardoferreira.jirareport.repository;

import br.com.leonardoferreira.jirareport.domain.LeadTime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadTimeRepository extends CrudRepository<LeadTime, Long> {

    void deleteByIssueId(Long id);

}
