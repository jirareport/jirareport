package br.com.leonardoferreira.jirareport.repository;

import br.com.leonardoferreira.jirareport.domain.Board;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lferreira
 * @since 3/23/18 6:21 PM
 */
@Repository
public interface BoardRepository extends CrudRepository<Board, Long> {

    @Override
    @EntityGraph(attributePaths = { "leadTimeConfigs" }, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Board> findById(Long id);

    Page<Board> findAll(Example<Board> example, Pageable pageable);

    void deleteByIdAndOwner(Long id, String username);

    @Query(value = "SELECT DISTINCT owner FROM BOARD", nativeQuery = true)
    List<String> findAllOwners();

}
