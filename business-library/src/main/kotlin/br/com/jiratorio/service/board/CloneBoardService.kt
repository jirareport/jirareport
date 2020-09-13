package br.com.jiratorio.service.board

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.service.DynamicFieldConfigService
import br.com.jiratorio.service.HolidayService
import br.com.jiratorio.service.LeadTimeConfigService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CloneBoardService(
    private val leadTimeConfigService: LeadTimeConfigService,
    private val holidayService: HolidayService,
    private val dynamicFieldConfigService: DynamicFieldConfigService,
    private val boardRepository: BoardRepository,
) {

    @Transactional
    fun clone(boardId: Long): Long {
        val boardToClone = boardRepository.findByIdOrNull(boardId) ?: throw ResourceNotFound()

        return boardToClone.copy(
            id = 0,
            leadTimeConfigs = mutableSetOf(),
            holidays = mutableListOf(),
            dynamicFields = mutableSetOf()
        )
            .also(boardRepository::save)
            .also { board -> boardToClone.leadTimeConfigs?.let { leadTimeConfigs -> leadTimeConfigService.clone(leadTimeConfigs, board) } }
            .also { board -> boardToClone.holidays?.let { holidays -> holidayService.clone(holidays, board) } }
            .also { board -> boardToClone.dynamicFields?.let { dynamicFields -> dynamicFieldConfigService.clone(dynamicFields, board) } }
            .let(BoardEntity::id)
    }

}
