package br.com.jiratorio.repository

import br.com.jiratorio.domain.dynamicfield.DynamicFieldsValues
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.request.SearchIssueRequest
import java.time.LocalDateTime

interface NativeIssueRepository {

    fun findByExample(board: Board, dynamicFilters: Map<String, Array<String>>, searchIssueRequest: SearchIssueRequest): List<Issue>

    fun findAllDynamicFieldValues(boardId: Long): List<DynamicFieldsValues>

    fun findAllEstimatesByBoardId(boardId: Long): Set<String>

    fun findAllSystemsByBoardId(boardId: Long): Set<String>

    fun findAllEpicsByBoardId(boardId: Long): Set<String>

    fun findAllIssueTypesByBoardId(boardId: Long): Set<String>

    fun findAllIssueProjectsByBoardId(boardId: Long): Set<String>

    fun findAllIssuePrioritiesByBoardId(boardId: Long): Set<String>

    fun findAllKeysByBoardIdAndDates(boardId: Long, startDate: LocalDateTime, endDate: LocalDateTime): Set<String>

}
