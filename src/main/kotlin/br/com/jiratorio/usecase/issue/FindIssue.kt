package br.com.jiratorio.usecase.issue

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.response.issue.IssueDetailResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.mapper.toIssueDetailResponse
import br.com.jiratorio.repository.IssueRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindIssue(
    private val issueRepository: IssueRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(id: Long, boardId: Long): IssueDetailResponse {
        log.info("Method=execute, id={}, boardId={}", id, boardId)

        val issue = issueRepository.findByBoardIdAndId(boardId, id)
            ?: throw ResourceNotFound()

        return issue.toIssueDetailResponse()
    }

}
