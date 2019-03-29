package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.Board
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface BoardRepository : CrudRepository<Board, Long> {

    @EntityGraph(attributePaths = ["leadTimeConfigs"], type = EntityGraph.EntityGraphType.LOAD)
    override fun findById(id: Long): Optional<Board>

    fun findByIdAndOwner(id: Long, owner: String): Board?

    fun findAll(specification: Specification<Board>, pageable: Pageable): Page<Board>

    @Query("SELECT DISTINCT b.owner FROM Board b")
    fun findAllOwners(): Set<String>

}
