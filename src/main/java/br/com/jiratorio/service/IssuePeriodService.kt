package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.domain.request.CreateIssuePeriodRequest
import br.com.jiratorio.domain.response.IssuePeriodResponse

interface IssuePeriodService {

    fun create(createIssuePeriodRequest: CreateIssuePeriodRequest, boardId: Long): Long?

    fun findById(id: Long): IssuePeriod

    fun remove(id: Long)

    fun update(id: Long)

    fun findIssuePeriodByBoard(boardId: Long): IssuePeriodResponse

}
