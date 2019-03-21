package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.LeadTimeConfig
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface LeadTimeConfigRepository : CrudRepository<LeadTimeConfig, Long> {

    fun findByBoardId(boardId: Long): List<LeadTimeConfig>

    fun findByIdAndBoardId(id: Long, boardId: Long): Optional<LeadTimeConfig>

}
