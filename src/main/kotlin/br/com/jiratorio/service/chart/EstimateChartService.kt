package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import rx.Single

interface EstimateChartService {

    fun leadTimeChartAsync(issues: List<Issue>, uninformed: String): Single<Chart<String, Double>>

    fun throughputChartAsync(issues: List<Issue>, uninformed: String): Single<Chart<String, Int>>

}
