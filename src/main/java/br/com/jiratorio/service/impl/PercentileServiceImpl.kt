package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.Percentile
import br.com.jiratorio.extension.logger
import br.com.jiratorio.service.PercentileService
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class PercentileServiceImpl : PercentileService {

    private val log = logger()

    override fun calculatePercentile(leadTimes: List<Long>): Percentile {
        log.info("Method=calculatePercentile, leadTimes={}", leadTimes)

        if (leadTimes.isEmpty()) {
            return Percentile()
        }

        val medianIndex = calculateCeilingPercentage(leadTimes.size, 50)
        val percentile75Index = calculateCeilingPercentage(leadTimes.size, 75)
        val percentile90Index = calculateCeilingPercentage(leadTimes.size, 90)

        val sortedLeadTimes = leadTimes.sorted()

        val median = sortedLeadTimes[medianIndex - 1]
        val percentile75 = sortedLeadTimes[percentile75Index - 1]
        val percentile90 = sortedLeadTimes[percentile90Index - 1]

        return Percentile(leadTimes.average(), median, percentile75, percentile90)
    }

    private fun calculateCeilingPercentage(totalElements: Int, percentage: Int): Int {
        return ceil(totalElements * percentage / 100.0).toInt()
    }

}
