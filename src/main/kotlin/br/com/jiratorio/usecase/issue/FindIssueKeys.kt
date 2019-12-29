package br.com.jiratorio.usecase.issue

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.response.issue.IssueKeysResponse
import br.com.jiratorio.extension.time.atEndOfDay
import br.com.jiratorio.repository.IssueRepository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@UseCase
class FindIssueKeys(
    private val issueRepository: IssueRepository
) {

    @Transactional(readOnly = true)
    fun execute(boardId: Long, startDate: LocalDate, endDate: LocalDate): IssueKeysResponse {
        return IssueKeysResponse(
            keys = issueRepository.findAllKeysByBoardIdAndDates(
                boardId = boardId,
                startDate = startDate.atStartOfDay(),
                endDate = endDate.atEndOfDay()
            )
        )
    }

}
