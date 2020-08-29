package br.com.jiratorio.domain.response.issueperiod

import br.com.jiratorio.domain.chart.IssuePeriodChartResponse

data class IssuePeriodListResponse(

    val periods: List<IssuePeriodResponse>,

    val charts: IssuePeriodChartResponse

)
