package br.com.jiratorio.service

import br.com.jiratorio.domain.request.CreateIssuePeriodRequest

interface CreateIssueService {

    fun create(createIssuePeriodRequest: CreateIssuePeriodRequest, boardId: Long): Long

}
