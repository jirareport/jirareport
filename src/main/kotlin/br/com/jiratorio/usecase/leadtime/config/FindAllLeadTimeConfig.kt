package br.com.jiratorio.usecase.leadtime.config

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.response.LeadTimeConfigResponse
import br.com.jiratorio.extension.log
import br.com.jiratorio.mapper.toLeadTimeConfigResponse
import br.com.jiratorio.repository.LeadTimeConfigRepository
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindAllLeadTimeConfig(
    private val leadTimeConfigRepository: LeadTimeConfigRepository
) {

    @Transactional(readOnly = true)
    fun execute(boardId: Long): List<LeadTimeConfigResponse> {
        log.info("Method=execute, boardId={}", boardId)

        return leadTimeConfigRepository
            .findByBoardId(boardId)
            .toLeadTimeConfigResponse()
    }

}
