package br.com.jiratorio.usecase.issue.period

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.request.CreateIssuePeriodRequest
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.repository.IssuePeriodRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class UpdateIssuePeriod(
    private val issuePeriodRepository: IssuePeriodRepository,
    private val createIssuePeriod: CreateIssuePeriod
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(id: Long, boardId: Long) {
        log.info("Action=updateIssuePeriod, id={}, boardId={}", boardId, id)

        val issuePeriod = issuePeriodRepository.findByIdAndBoardId(id, boardId)
            ?: throw ResourceNotFound()

        val createIssuePeriodRequest = CreateIssuePeriodRequest(issuePeriod.startDate, issuePeriod.endDate)

        createIssuePeriod.execute(createIssuePeriodRequest, boardId)
    }

}
