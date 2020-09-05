package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.DynamicFieldConfigEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DynamicFieldConfigRepository : CrudRepository<DynamicFieldConfigEntity, Long> {

    fun findByBoard(board: BoardEntity): List<DynamicFieldConfigEntity>

    fun findByBoardIdAndId(boardId: Long, id: Long): DynamicFieldConfigEntity?

    fun findByIdOrNull(id: Long): DynamicFieldConfigEntity? =
        findById(id).orElse(null)

}
