package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart

interface IssueTypeChartService {

    fun leadTimeByType(issues: List<Issue>): Chart<String, Double>

    fun throughputByType(issues: List<Issue>): Chart<String, Int>

}
