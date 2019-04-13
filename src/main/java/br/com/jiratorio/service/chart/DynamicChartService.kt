package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.dynamicfield.DynamicChart
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue

interface DynamicChartService {

    fun buildDynamicCharts(issues: List<Issue>, board: Board): List<DynamicChart>

}
