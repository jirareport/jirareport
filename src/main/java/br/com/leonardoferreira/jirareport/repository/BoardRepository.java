package br.com.leonardoferreira.jirareport.repository;

import java.util.Optional;

import br.com.leonardoferreira.jirareport.domain.Board;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lferreira
 * @since 3/23/18 6:21 PM
 */
@Repository
public interface BoardRepository extends CrudRepository<Board, Long> {

    @EntityGraph(attributePaths = { "leadTimeConfigs" }, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Board> findById(Long id);

    Page<Board> findAll(Example<Board> example, Pageable pageable);

    void deleteByIdAndOwner(Long id, String username);
}
