package br.com.jiratorio.repository;

import java.util.List;

import br.com.jiratorio.domain.LeadTimeConfig;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadTimeConfigRepository extends CrudRepository<LeadTimeConfig, Long> {

    List<LeadTimeConfig> findByBoardId(Long boardId);

    Optional<LeadTimeConfig> findByIdAndBoardId(Long id, Long boardId);

}
