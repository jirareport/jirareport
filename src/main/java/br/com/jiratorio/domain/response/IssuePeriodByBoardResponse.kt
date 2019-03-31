package br.com.jiratorio.domain.response

import br.com.jiratorio.domain.chart.IssuePeriodChartResponse

data class IssuePeriodByBoardResponse(
    val periods: List<IssuePeriodResponse>,
    val charts: IssuePeriodChartResponse
)
