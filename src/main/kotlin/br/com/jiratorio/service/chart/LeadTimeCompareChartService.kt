package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import rx.Single

interface LeadTimeCompareChartService {

    fun leadTimeCompareAsync(issues: List<Issue>): Single<Chart<String, Double>>

}
