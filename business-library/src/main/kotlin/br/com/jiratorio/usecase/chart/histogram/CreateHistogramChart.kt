package br.com.jiratorio.usecase.chart.histogram

import br.com.jiratorio.domain.MinimalIssue
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.entity.embedded.Histogram
import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.usecase.percentile.CalculatePercentile
import org.slf4j.LoggerFactory

@UseCase
class CreateHistogramChart(
    private val calculatePercentile: CalculatePercentile
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issues: List<MinimalIssue>): Histogram {
        log.info("Action=createHistogramChart, issues={}", issues)

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

    private fun histogramChart(issues: List<MinimalIssue>): Chart<Long, Int> {
        log.info("Method=histogramChart, issues={}", issues)

        val collect: MutableMap<Long, Int> = issues
            .groupingBy { it.leadTime }
            .eachCount()
            .toMutableMap()

        val max = collect.keys.maxOrNull() ?: 1L
        for (i in 1 until max) {
            collect.putIfAbsent(i, 0)
        }

        return Chart(collect)
    }
}
