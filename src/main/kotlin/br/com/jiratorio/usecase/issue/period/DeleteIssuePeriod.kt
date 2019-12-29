package br.com.jiratorio.usecase.issue.period

import br.com.jiratorio.config.stereotype.UseCase
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
        log.info("Method=execute, id={}, boardId={}", boardId, id)

        val issuePeriod = issuePeriodRepository.findByBoardIdAndId(boardId, id)
            ?: throw ResourceNotFound()

        issuePeriodRepository.delete(issuePeriod)
    }

}
