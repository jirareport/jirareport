package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.domain.request.CreateIssuePeriodRequest
import br.com.jiratorio.domain.response.IssuePeriodByBoardResponse

interface IssuePeriodService {

    fun create(createIssuePeriodRequest: CreateIssuePeriodRequest, boardId: Long): Long?

    fun findById(id: Long): IssuePeriod

    fun removeByBoardAndId(boardId: Long, id: Long)

    fun update(boardId: Long, id: Long)

    fun findIssuePeriodByBoard(boardId: Long): IssuePeriodByBoardResponse

}
