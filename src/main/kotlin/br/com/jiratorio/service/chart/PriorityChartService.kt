package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart

interface PriorityChartService {

    fun leadTimeByPriority(issues: List<Issue>, uninformed: String): Chart<String, Double>

    fun throughputByPriority(issues: List<Issue>, uninformed: String): Chart<String, Int>

}
