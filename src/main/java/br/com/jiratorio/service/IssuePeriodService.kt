package br.com.jiratorio.service

import br.com.jiratorio.domain.request.CreateIssuePeriodRequest
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodByBoardResponse
import br.com.jiratorio.domain.response.issueperiod.IssuePeriodByIdResponse

interface IssuePeriodService {

    fun create(createIssuePeriodRequest: CreateIssuePeriodRequest, boardId: Long): Long?

    fun findById(boardId: Long, id: Long): IssuePeriodByIdResponse

    fun removeByBoardAndId(boardId: Long, id: Long)

    fun update(boardId: Long, id: Long)

    fun findIssuePeriodByBoard(boardId: Long): IssuePeriodByBoardResponse

}
