package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.ColumnTimeAvg

interface ColumnTimeChartService {

    fun averageAsync(issues: List<Issue>, fluxColumn: List<String>): List<ColumnTimeAvg>

}
