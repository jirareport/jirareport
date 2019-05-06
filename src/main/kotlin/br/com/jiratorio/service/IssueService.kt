package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.request.SearchIssueRequest
import br.com.jiratorio.domain.response.IssueDetailResponse
import br.com.jiratorio.domain.response.IssueFilterResponse
import br.com.jiratorio.domain.response.ListIssueResponse
import java.time.LocalDate

interface IssueService {

    fun createByJql(jql: String, board: Board): List<Issue>

    fun findByExample(boardId: Long, searchIssueRequest: SearchIssueRequest): ListIssueResponse

    fun findLeadTimeByExample(boardId: Long, searchIssueRequest: SearchIssueRequest): List<Long>

    fun deleteAll(issues: List<Issue>)

    fun findFilters(boardId: Long, startDate: LocalDate, endDate: LocalDate): IssueFilterResponse

    fun findByBoardAndId(boardId: Long, id: Long): IssueDetailResponse

}
