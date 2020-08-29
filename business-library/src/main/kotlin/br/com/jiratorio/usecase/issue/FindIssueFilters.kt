package br.com.jiratorio.usecase.issue

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.response.issue.IssueFilterResponse
import br.com.jiratorio.repository.IssueRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindIssueFilters(
    private val issueRepository: IssueRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(boardId: Long): IssueFilterResponse {
        log.info("Action=findIssueFilters, boardId={}", boardId)

        return IssueFilterResponse(
            estimates = issueRepository.findAllEstimatesByBoardId(boardId),
            systems = issueRepository.findAllSystemsByBoardId(boardId),
            epics = issueRepository.findAllEpicsByBoardId(boardId),
            issueTypes = issueRepository.findAllIssueTypesByBoardId(boardId),
            projects = issueRepository.findAllIssueProjectsByBoardId(boardId),
            priorities = issueRepository.findAllIssuePrioritiesByBoardId(boardId),
            dynamicFieldsValues = issueRepository.findAllDynamicFieldValues(boardId)
        )
    }
}
