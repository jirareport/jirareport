package br.com.leonardoferreira.jirareport.repository;

import java.util.Optional;

import br.com.leonardoferreira.jirareport.domain.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lferreira
 * @since 3/23/18 6:21 PM
 */
@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    @EntityGraph(attributePaths = { "leadTimeConfigs" }, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Project> findById(Long id);
}
