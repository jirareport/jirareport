package br.com.jiratorio.usecase.issue.period

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.repository.IssuePeriodRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class DeleteIssuePeriod(
    private val issuePeriodRepository: IssuePeriodRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(id: Long, boardId: Long) {
        log.info("Action=deleteIssuePeriod, id={}, boardId={}", boardId, id)

        val issuePeriod = issuePeriodRepository.findByIdAndBoardId(id, boardId)
            ?: throw ResourceNotFound()

        issuePeriodRepository.delete(issuePeriod)
    }

}
