package br.com.jiratorio.service.leadtime

import br.com.jiratorio.domain.entity.Issue

interface LeadTimeService {

    fun createLeadTimes(issues: List<Issue>, boardId: Long)

}
