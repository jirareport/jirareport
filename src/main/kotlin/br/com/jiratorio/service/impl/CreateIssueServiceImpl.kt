package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.request.CreateIssuePeriodRequest
import br.com.jiratorio.service.CreateIssueService
import br.com.jiratorio.usecase.issue.period.CreateIssuePeriod
import org.springframework.stereotype.Service

@Service
class CreateIssueServiceImpl(
    private val createIssuePeriod: CreateIssuePeriod
) : CreateIssueService {

    override fun create(createIssuePeriodRequest: CreateIssuePeriodRequest, boardId: Long): Long =
        createIssuePeriod.execute(createIssuePeriodRequest, boardId)

}
