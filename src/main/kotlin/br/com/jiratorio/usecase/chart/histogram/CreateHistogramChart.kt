package br.com.jiratorio.usecase.chart.histogram

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.entity.embedded.Histogram
import br.com.jiratorio.usecase.percentile.CalculatePercentile
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CreateHistogramChart(
    private val calculatePercentile: CalculatePercentile
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<Issue>): Histogram {
        log.info("Method=execute, issues={}", issues)

        val leadTimeList = issues.map { it.leadTime }
        val percentile = calculatePercentile.execute(leadTimeList)
        val chart = histogramChart(issues)

        return Histogram(
            chart = chart,
            median = percentile.median,
            percentile75 = percentile.percentile75,
            percentile90 = percentile.percentile90
        )
    }

    private fun histogramChart(issues: List<Issue>): Chart<Long, Int> {
        log.info("Method=histogramChart, issues={}", issues)

        val collect: MutableMap<Long, Int> = issues
            .groupingBy { it.leadTime }
            .eachCount()
            .toMutableMap()

        val max = collect.keys.max() ?: 1L
        for (i in 1 until max) {
            collect.putIfAbsent(i, 0)
        }

        return Chart(collect)
    }
}
