package br.com.leonardoferreira.jirareport.repository;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.LeadTimeConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lferreira on 17/05/18
 */
@Repository
public interface LeadTimeConfigRepository extends CrudRepository<LeadTimeConfig, Long> {

    List<LeadTimeConfig> findByProjectId(Long projectId);

    LeadTimeConfig findByIdAndProjectId(Long id, Long projectId);

    void deleteByIdAndProjectId(Long id, Long projectId);

}
