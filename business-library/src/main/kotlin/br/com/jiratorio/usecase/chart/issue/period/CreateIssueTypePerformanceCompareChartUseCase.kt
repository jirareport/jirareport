package br.com.jiratorio.usecase.chart.issue.period

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.chart.MultiAxisChart
import br.com.jiratorio.domain.entity.IssuePeriodEntity
import br.com.jiratorio.extension.decimal.zeroIfNaN
import org.slf4j.LoggerFactory

@UseCase
class CreateIssueTypePerformanceCompareChartUseCase {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issuePeriods: List<IssuePeriodEntity>): Map<String, MultiAxisChart<Number>> {
        log.info("Action=createIssueTypePerformanceCompareChart, issuePeriods={}", issuePeriods)

        return issuePeriods.issueTypes
            .map { issueType -> buildChartByIssueType(issueType, issuePeriods) }
            .toMap()
    }

    private val List<IssuePeriodEntity>.issueTypes: Sequence<String>
        get() = asSequence()
            .flatMap { issuePeriod -> issuePeriod.issues.asSequence() }
            .map { issue -> issue.issueType ?: EMPTY }
            .distinct()

    private fun buildChartByIssueType(issueType: String, issuePeriods: List<IssuePeriodEntity>): Pair<String, MultiAxisChart<Number>> {
        val result = MultiAxisChart<Number>()

        issuePeriods.forEach {
            val leadTimes = it.issues
                .filter { issue -> issue.issueType ?: EMPTY == issueType }
                .map { issue -> issue.leadTime }

            result[it.name] = mapOf(
                "Throughput" to leadTimes.size,
                "Lead Time" to leadTimes.average().zeroIfNaN()
            )
        }

        return issueType to result
    }

    companion object {
        private const val EMPTY: String = "<<EMPTY>>"
    }

}
