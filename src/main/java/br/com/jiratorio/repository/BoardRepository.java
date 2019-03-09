package br.com.jiratorio.repository;

import br.com.jiratorio.domain.entity.Board;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends CrudRepository<Board, Long> {

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"leadTimeConfigs"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Board> findById(@NonNull Long id);

    Page<Board> findAll(Example<Board> example, Pageable pageable);

    void deleteByIdAndOwner(Long id, String username);

    @Query("SELECT DISTINCT b.owner FROM Board b")
    List<String> findAllOwners();

}
