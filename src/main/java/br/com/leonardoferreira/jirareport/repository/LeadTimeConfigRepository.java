package br.com.leonardoferreira.jirareport.repository;

import java.util.List;

import br.com.leonardoferreira.jirareport.domain.LeadTimeConfig;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadTimeConfigRepository extends CrudRepository<LeadTimeConfig, Long> {

    List<LeadTimeConfig> findByBoardId(Long boardId);

    Optional<LeadTimeConfig> findByIdAndBoardId(Long id, Long boardId);

}
