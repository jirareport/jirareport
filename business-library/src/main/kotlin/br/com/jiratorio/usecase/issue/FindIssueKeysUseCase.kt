package br.com.jiratorio.usecase.issue

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.response.issue.IssueKeysResponse
import br.com.jiratorio.extension.time.atEndOfDay
import br.com.jiratorio.repository.IssueRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@UseCase
class FindIssueKeysUseCase(
    private val issueRepository: IssueRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(boardId: Long, startDate: LocalDate, endDate: LocalDate): IssueKeysResponse {
        log.info("Action=findIssueKeys, boardId={}, startDate={}, endDate={}", boardId, startDate, endDate)

        return IssueKeysResponse(
            keys = issueRepository.findAllKeysByBoardIdAndDates(
                boardId = boardId,
                startDate = startDate.atStartOfDay(),
                endDate = endDate.atEndOfDay()
            )
        )
    }

}
