package br.com.jiratorio.usecase.leadtime.config

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.request.LeadTimeConfigRequest
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.mapper.updateFromLeadTimeConfigRequest
import br.com.jiratorio.repository.LeadTimeConfigRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class UpdateLeadTimeConfig(
    private val leadTimeConfigRepository: LeadTimeConfigRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(id: Long, boardId: Long, leadTimeConfigRequest: LeadTimeConfigRequest) {
        log.info("Action=updateLeadTimeConfig, id={}, boardId={}, leadTimeConfigRequest={}", id, boardId, leadTimeConfigRequest)

        val leadTimeConfig = leadTimeConfigRepository.findByIdAndBoardId(id, boardId)
            ?: throw ResourceNotFound()

        leadTimeConfig.updateFromLeadTimeConfigRequest(leadTimeConfigRequest)
        leadTimeConfigRepository.save(leadTimeConfig)
    }

}
