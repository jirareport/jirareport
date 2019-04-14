package br.com.jiratorio.service.chart

import br.com.jiratorio.domain.dynamicfield.DynamicChart
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import kotlinx.coroutines.Deferred

interface DynamicChartService {

    fun buildDynamicChartsAsync(issues: List<Issue>, board: Board, uninformed: String): Deferred<List<DynamicChart>>

}
