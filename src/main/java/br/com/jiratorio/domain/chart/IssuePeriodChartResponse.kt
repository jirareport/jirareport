package br.com.jiratorio.domain.chart

import br.com.jiratorio.domain.entity.IssuePeriod
import br.com.jiratorio.domain.entity.embedded.Chart

class IssuePeriodChartResponse(
    val issueCountBySize: IssueCountBySize,
    val leadTimeCompareChart: LeadTimeCompareChart,
    val leadtime: Chart<String, String> = Chart(),
    val issuesCount: Chart<String, Int> = Chart()
) {

    fun addLeadTime(issuePeriod: IssuePeriod) {
        leadtime.add(issuePeriod.dates, "%.2f".format(issuePeriod.avgLeadTime))
    }

    fun addIssuesCount(issuePeriod: IssuePeriod) {
        issuesCount.add(issuePeriod.dates, issuePeriod.issuesCount)
    }

}
