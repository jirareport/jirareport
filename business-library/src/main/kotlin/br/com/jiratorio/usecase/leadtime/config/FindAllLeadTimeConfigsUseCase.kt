package br.com.jiratorio.usecase.leadtime.config

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.response.LeadTimeConfigResponse
import br.com.jiratorio.mapper.toLeadTimeConfigResponse
import br.com.jiratorio.repository.LeadTimeConfigRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindAllLeadTimeConfigsUseCase(
    private val leadTimeConfigRepository: LeadTimeConfigRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(boardId: Long): List<LeadTimeConfigResponse> {
        log.info("Action=findAllLeadTimeConfigs, boardId={}", boardId)

        return leadTimeConfigRepository
            .findByBoardId(boardId)
            .toLeadTimeConfigResponse()
    }

}
