package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.ColumnTimeAvg

interface ColumnTimeChartService {

    fun average(issues: List<Issue>, fluxColumn: List<String>): List<ColumnTimeAvg>

}
