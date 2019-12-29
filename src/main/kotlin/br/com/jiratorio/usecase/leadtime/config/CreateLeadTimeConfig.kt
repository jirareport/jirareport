package br.com.jiratorio.usecase.leadtime.config

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.request.LeadTimeConfigRequest
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.log
import br.com.jiratorio.mapper.toLeadTimeConfig
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.repository.LeadTimeConfigRepository
import org.springframework.transaction.annotation.Transactional

@UseCase
class CreateLeadTimeConfig(
    private val boardRepository: BoardRepository,
    private val leadTimeConfigRepository: LeadTimeConfigRepository
) {

    @Transactional
    fun execute(boardId: Long, leadTimeConfigRequest: LeadTimeConfigRequest): Long {
        log.info("Method=create, boardId={}, leadTimeConfigRequest={}", boardId, leadTimeConfigRequest)

        val board = boardRepository.findByIdOrNull(boardId) ?: throw ResourceNotFound()
        val leadTimeConfig = leadTimeConfigRequest.toLeadTimeConfig(board)

        leadTimeConfigRepository.save(leadTimeConfig)

        return leadTimeConfig.id
    }

}
