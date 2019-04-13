package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.chart.IssueCountBySize
import br.com.jiratorio.domain.chart.LeadTimeCompareChart
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.IssuePeriod

interface IssuePeriodChartService {

    fun leadTimeCompareByPeriod(issuePeriods: List<IssuePeriod>, board: Board): LeadTimeCompareChart

    fun issueCountBySize(issuePeriods: List<IssuePeriod>): IssueCountBySize

}
