package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart

interface SystemChartService {

    fun leadTimeBySystem(issues: List<Issue>): Chart<String, Double>

    fun throughputBySystem(issues: List<Issue>): Chart<String, Int>

}
