package br.com.jiratorio.repository

import br.com.jiratorio.domain.FindAllIssuePeriodsFilter
import br.com.jiratorio.domain.issue.MinimalIssuePeriod

internal interface NativeIssuePeriodRepository {

    fun findAll(filter: FindAllIssuePeriodsFilter): List<MinimalIssuePeriod>

}
