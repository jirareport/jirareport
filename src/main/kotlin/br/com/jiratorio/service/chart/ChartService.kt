package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.chart.ChartAggregator
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue

interface ChartService {

    fun buildAllCharts(issues: List<Issue>, board: Board): ChartAggregator

}
