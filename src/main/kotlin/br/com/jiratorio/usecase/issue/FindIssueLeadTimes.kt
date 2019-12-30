package br.com.jiratorio.usecase.issue

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.repository.IssueRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindIssueLeadTimes(
    private val issueRepository: IssueRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(board: Board, searchIssueRequest: SearchIssueRequest): List<Long> {
        log.info("Method=findLeadTimeByExample, board={}, searchIssueRequest={}", board, searchIssueRequest)
        return issueRepository.findByExample(board, emptyMap(), searchIssueRequest)
            .map { it.leadTime }
    }

}
