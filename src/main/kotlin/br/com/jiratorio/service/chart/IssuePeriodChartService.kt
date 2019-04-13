package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.chart.IssueCountByEstimate
import br.com.jiratorio.domain.chart.LeadTimeCompareChart
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.IssuePeriod

interface IssuePeriodChartService {

    fun leadTimeCompareByPeriod(issuePeriods: List<IssuePeriod>, board: Board): LeadTimeCompareChart

    fun issueCountByEstimate(issuePeriods: List<IssuePeriod>): IssueCountByEstimate

}
