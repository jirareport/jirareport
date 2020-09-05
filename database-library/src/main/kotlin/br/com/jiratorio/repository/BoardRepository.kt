package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.BoardEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface BoardRepository : CrudRepository<BoardEntity, Long> {

    @EntityGraph(attributePaths = ["leadTimeConfigs"], type = EntityGraph.EntityGraphType.LOAD)
    override fun findById(id: Long): Optional<BoardEntity>

    fun findByIdAndOwner(id: Long, owner: String): BoardEntity?

    fun findAll(specification: Specification<BoardEntity>, pageable: Pageable): Page<BoardEntity>

    @Query("SELECT DISTINCT b.owner FROM BoardEntity b")
    fun findAllOwners(): Set<String>

    fun findByIdOrNull(id: Long): BoardEntity? =
        findById(id).orElse(null)

}
