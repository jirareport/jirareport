package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import kotlinx.coroutines.Deferred

interface LeadTimeCompareChartService {

    fun leadTimeCompareAsync(issues: List<Issue>): Deferred<Chart<String, Double>>

}
