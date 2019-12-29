package br.com.jiratorio.usecase.leadtime.config

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.response.LeadTimeConfigResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.log
import br.com.jiratorio.mapper.toLeadTimeConfigResponse
import br.com.jiratorio.repository.LeadTimeConfigRepository
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindLeadTimeConfig(
    private val leadTimeConfigRepository: LeadTimeConfigRepository
) {

    @Transactional(readOnly = true)
    fun execute(id: Long, boardId: Long): LeadTimeConfigResponse {
        log.info("Method=execute, id={}, boardId={}", id, boardId)

        val leadTimeConfig = leadTimeConfigRepository.findByIdAndBoardId(id, boardId)
            ?: throw ResourceNotFound()

        return leadTimeConfig.toLeadTimeConfigResponse()
    }

}
