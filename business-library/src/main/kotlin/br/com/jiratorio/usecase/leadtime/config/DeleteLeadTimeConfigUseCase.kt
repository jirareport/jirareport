package br.com.jiratorio.usecase.leadtime.config

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.repository.LeadTimeConfigRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class DeleteLeadTimeConfigUseCase(
    private val leadTimeConfigRepository: LeadTimeConfigRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(id: Long, boardId: Long) {
        log.info("Action=deleteLeadTimeConfig, id={}, boardId={}", id, boardId)

        val leadTimeConfig = leadTimeConfigRepository.findByIdAndBoardId(id, boardId)
            ?: throw ResourceNotFound()

        leadTimeConfigRepository.delete(leadTimeConfig)
    }

}
