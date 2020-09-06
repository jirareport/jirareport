package br.com.jiratorio.usecase.chart.issue.period

import br.com.jiratorio.domain.chart.MultiAxisChart
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.IssuePeriodEntity
import br.com.jiratorio.internationalization.MessageResolver
import br.com.jiratorio.mapper.toMultiAxisChart
import br.com.jiratorio.repository.IssueRepository
import br.com.jiratorio.stereotype.UseCase
import org.slf4j.LoggerFactory

@UseCase
class CreateThroughputByEstimateChartUseCase(
    private val issueRepository: IssueRepository,
    private val messageResolver: MessageResolver,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(issuePeriods: List<IssuePeriodEntity>, board: BoardEntity): MultiAxisChart<Int> {
        log.info("Action=createThroughputByEstimateChart, issuePeriods={}", issuePeriods)

        if (board.estimateCF.isNullOrBlank()) {
            return MultiAxisChart()
        }

        val uninformedValue = messageResolver.resolve("uninformed")
        return issueRepository.findThroughputByPeriodAndEstimate(board.id, issuePeriods.map { it.id })
            .groupBy { board.issuePeriodNameFormat.format(it.periodStart, it.periodEnd) }
            .mapValues { entry -> entry.value.associate { Pair(it.estimate ?: uninformedValue, it.throughput) } }
            .toMultiAxisChart()
    }

}
