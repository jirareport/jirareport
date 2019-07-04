package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart

interface ProjectChartService {

    fun leadTimeByProject(issues: List<Issue>, uninformed: String): Chart<String, Double>

    fun throughputByProject(issues: List<Issue>, uninformed: String): Chart<String, Int>

}
