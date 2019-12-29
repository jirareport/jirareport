package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.domain.response.ListIssueResponse
import br.com.jiratorio.domain.response.issue.IssueDetailResponse
import br.com.jiratorio.domain.response.issue.IssueKeysResponse
import br.com.jiratorio.domain.response.issue.IssueFilterResponse
import java.time.LocalDate

interface IssueService {

    fun findByExample(
        boardId: Long,
        dynamicFilters: Map<String, Array<String>>,
        searchIssueRequest: SearchIssueRequest
    ): ListIssueResponse

    fun findLeadTimeByExample(board: Board, searchIssueRequest: SearchIssueRequest): List<Long>

    fun deleteAll(issues: Set<Issue>)

    fun findByBoardAndId(boardId: Long, id: Long): IssueDetailResponse

    fun findFilters(boardId: Long): IssueFilterResponse

    fun findFilterKeys(boardId: Long, startDate: LocalDate, endDate: LocalDate): IssueKeysResponse

}
