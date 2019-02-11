package br.com.jiratorio.repository;

import br.com.jiratorio.domain.LeadTime;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadTimeRepository extends CrudRepository<LeadTime, Long> {

    void deleteByIssueId(Long id);

}
