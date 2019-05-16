package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart

interface LeadTimeCompareChartService {

    fun leadTimeCompareAsync(issues: List<Issue>): Chart<String, Double>

}
