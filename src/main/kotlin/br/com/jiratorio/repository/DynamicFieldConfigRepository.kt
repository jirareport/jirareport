package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.DynamicFieldConfig
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DynamicFieldConfigRepository : CrudRepository<DynamicFieldConfig, Long> {

    fun findByBoard(board: Board): List<DynamicFieldConfig>

    fun findByBoardIdAndId(boardId: Long, id: Long): DynamicFieldConfig?

    @JvmDefault
    fun findByIdOrNull(id: Long): DynamicFieldConfig? =
        findById(id).orElse(null)

}
