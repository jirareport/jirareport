package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart

interface EstimateChartService {

    fun leadTimeChart(issues: List<Issue>): Chart<String, Double>

    fun throughputChart(issues: List<Issue>): Chart<String, Int>

}
