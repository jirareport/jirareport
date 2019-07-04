package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart

interface SystemChartService {

    fun leadTimeBySystem(issues: List<Issue>, uninformed: String): Chart<String, Double>

    fun throughputBySystem(issues: List<Issue>, uninformed: String): Chart<String, Int>

}
