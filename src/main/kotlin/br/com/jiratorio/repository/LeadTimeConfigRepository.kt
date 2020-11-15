package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.LeadTimeConfigEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LeadTimeConfigRepository : CrudRepository<LeadTimeConfigEntity, Long> {

    fun findByBoardId(boardId: Long): List<LeadTimeConfigEntity>

    fun findByIdAndBoardId(id: Long, boardId: Long): LeadTimeConfigEntity?

    fun findByIdOrNull(id: Long): LeadTimeConfigEntity? =
        findById(id).orElse(null)

}
