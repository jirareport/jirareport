package br.com.jiratorio.domain.response

import br.com.jiratorio.domain.chart.IssuePeriodChartResponse
import br.com.jiratorio.domain.entity.IssuePeriod

data class IssuePeriodResponse(
    val issuePeriods: List<IssuePeriod>,
    val issuePeriodChartResponse: IssuePeriodChartResponse
)
