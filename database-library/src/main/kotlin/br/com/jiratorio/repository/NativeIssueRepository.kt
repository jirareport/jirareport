package br.com.jiratorio.repository

import br.com.jiratorio.domain.DynamicFieldsValues
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.issue.Issue
import br.com.jiratorio.domain.request.SearchIssueRequest
import java.time.LocalDateTime

internal interface NativeIssueRepository {

    fun findByExample(board: BoardEntity, dynamicFilters: Map<String, Array<String>>, searchIssueRequest: SearchIssueRequest): List<Issue>

    fun findAllDynamicFieldValues(boardId: Long): List<DynamicFieldsValues>

    fun findAllEstimatesByBoardId(boardId: Long): Set<String>

    fun findAllSystemsByBoardId(boardId: Long): Set<String>

    fun findAllEpicsByBoardId(boardId: Long): Set<String>

    fun findAllIssueTypesByBoardId(boardId: Long): Set<String>

    fun findAllIssueProjectsByBoardId(boardId: Long): Set<String>

    fun findAllIssuePrioritiesByBoardId(boardId: Long): Set<String>

    fun findAllKeysByBoardIdAndDates(boardId: Long, startDate: LocalDateTime, endDate: LocalDateTime): Set<String>

}
