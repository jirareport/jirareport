package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.domain.response.ListIssueResponse
import br.com.jiratorio.domain.response.issue.IssueDetailResponse
import br.com.jiratorio.domain.response.issue.IssueKeysResponse
import br.com.jiratorio.domain.response.issue.IssueFilterResponse
import br.com.jiratorio.service.IssueService
import br.com.jiratorio.usecase.issue.DeleteIssue
import br.com.jiratorio.usecase.issue.FindIssue
import br.com.jiratorio.usecase.issue.FindIssueByExample
import br.com.jiratorio.usecase.issue.FindIssueKeys
import br.com.jiratorio.usecase.issue.FindIssueFilters
import br.com.jiratorio.usecase.issue.FindIssueLeadTimeByExample
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class IssueServiceImpl(
    private val deleteIssue: DeleteIssue,
    private val findIssue: FindIssue,
    private val findIssueByExample: FindIssueByExample,
    private val findIssueKeys: FindIssueKeys,
    private val findIssueFilters: FindIssueFilters,
    private val findIssueLeadTimeByExample: FindIssueLeadTimeByExample
) : IssueService {

    override fun findByExample(
        boardId: Long,
        dynamicFilters: Map<String, Array<String>>,
        searchIssueRequest: SearchIssueRequest
    ): ListIssueResponse =
        findIssueByExample.execute(boardId, dynamicFilters, searchIssueRequest)

    override fun findLeadTimeByExample(board: Board, searchIssueRequest: SearchIssueRequest): List<Long> =
        findIssueLeadTimeByExample.execute(board, searchIssueRequest)

    override fun deleteAll(issues: Set<Issue>) =
        deleteIssue.execute(issues)

    override fun findByBoardAndId(boardId: Long, id: Long): IssueDetailResponse =
        findIssue.execute(id, boardId)

    override fun findFilters(boardId: Long): IssueFilterResponse =
        findIssueFilters.execute(boardId)

    override fun findFilterKeys(boardId: Long, startDate: LocalDate, endDate: LocalDate): IssueKeysResponse =
        findIssueKeys.execute(boardId, startDate, endDate)

}
