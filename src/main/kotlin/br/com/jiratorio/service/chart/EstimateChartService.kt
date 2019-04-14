package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import kotlinx.coroutines.Deferred

interface EstimateChartService {

    fun leadTimeChartAsync(issues: List<Issue>, uninformed: String): Deferred<Chart<String, Double>>

    fun throughputChartAsync(issues: List<Issue>, uninformed: String): Deferred<Chart<String, Int>>

}
