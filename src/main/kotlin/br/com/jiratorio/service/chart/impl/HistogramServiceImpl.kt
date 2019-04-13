package br.com.jiratorio.service.chart.impl

import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.Chart
import br.com.jiratorio.domain.entity.embedded.Histogram
import br.com.jiratorio.extension.log
import br.com.jiratorio.service.PercentileService
import br.com.jiratorio.service.chart.HistogramService
import org.springframework.stereotype.Service

@Service
class HistogramServiceImpl(
    private val percentileService: PercentileService
) : HistogramService {

    override fun issueHistogram(issues: List<Issue>): Histogram {
        log.info("Method=issueHistogram, issues={}", issues)

        val leadTimeList = issues.map { it.leadTime }
        val percentile = percentileService.calculatePercentile(leadTimeList)
        val chart = histogramChart(issues)

        return Histogram(
            chart,
            percentile.median,
            percentile.percentile75,
            percentile.percentile90
        )
    }

    private fun histogramChart(issues: List<Issue>): Chart<Long, Int> {
        log.info("Method=histogramChart, issues={}", issues)

        val collect: MutableMap<Long, Int> = issues
            .groupingBy {
                it.leadTime
            }
            .eachCount()
            .toMutableMap()

        val max = collect.keys.max() ?: 1L
        for (i in 1 until max) {
            collect.putIfAbsent(i, 0)
        }

        return Chart(collect)
    }
}
