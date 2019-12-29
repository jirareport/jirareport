package br.com.jiratorio.service.leadtime.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.service.leadtime.LeadTimeService
import br.com.jiratorio.usecase.leadtime.CreateLeadTime
import org.springframework.stereotype.Service

@Service
class LeadTimeServiceImpl(
    private val createLeadTime: CreateLeadTime
) : LeadTimeService {

    override fun createLeadTimes(issues: List<Issue>, boardId: Long) =
        createLeadTime.execute(issues, boardId)

}
