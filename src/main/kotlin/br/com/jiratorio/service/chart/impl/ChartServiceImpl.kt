package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.domain.chart.ChartAggregator
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.service.chart.ChartService
import br.com.jiratorio.usecase.chart.CreateChartAggregator
import org.springframework.stereotype.Service

@Service
class ChartServiceImpl(
    private val createChartAggregator: CreateChartAggregator
) : ChartService {

    override fun buildAllCharts(issues: List<Issue>, board: Board): ChartAggregator =
        createChartAggregator.execute(issues, board)

}
