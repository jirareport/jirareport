package br.com.jiratorio.usecase.issue.period

import br.com.jiratorio.config.property.JiraProperties
import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodByIdResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.mapper.toIssuePeriodDetailResponse
import br.com.jiratorio.mapper.toIssueResponse
import br.com.jiratorio.repository.IssuePeriodRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindIssuePeriod(
    private val issuePeriodRepository: IssuePeriodRepository,
    private val jiraProperties: JiraProperties
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(id: Long, boardId: Long): IssuePeriodByIdResponse {
        log.info("Action=findIssuePeriod, id={}, boardId={}", boardId, id)

        val issuePeriod = issuePeriodRepository.findByIdAndBoardId(id, boardId)
            ?: throw ResourceNotFound()

        return IssuePeriodByIdResponse(
            detail = issuePeriod.toIssuePeriodDetailResponse(),
            issues = issuePeriod.issues.toIssueResponse(jiraProperties.url)
        )
    }

}
