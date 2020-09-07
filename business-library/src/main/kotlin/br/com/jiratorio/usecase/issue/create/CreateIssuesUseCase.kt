package br.com.jiratorio.usecase.issue.create

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.IssueEntity
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.domain.impediment.calculator.ImpedimentCalculatorResult
import br.com.jiratorio.domain.parsed.ParsedIssue
import br.com.jiratorio.extension.time.daysDiff
import br.com.jiratorio.service.DueDateService
import br.com.jiratorio.service.EfficiencyService
import br.com.jiratorio.usecase.leadtime.CreateLeadTimeUseCase
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@UseCase
class CreateIssuesUseCase(
    private val findAllOpenIssues: FindAllOpenIssuesUseCase,
    private val createLeadTime: CreateLeadTimeUseCase,
    private val findHolidayDays: FindHolidayDaysUseCase,
    private val dueDateService: DueDateService,
    private val efficiencyService: EfficiencyService,
    private val persistIssue: PersistIssueUseCase,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(board: BoardEntity, issuePeriodId: Long, startDate: LocalDate, endDate: LocalDate): Pair<String, List<IssueEntity>> {
        log.info("Action=createIssues, board={}, issuePeriodId={}, startDate={}, endDate={}", board, issuePeriodId, startDate, endDate)

        val holidays = findHolidayDays.execute(board.id)

        val (jql, parsedIssues) = findAllOpenIssues.execute(board, holidays, startDate, endDate)

        val issues = parsedIssues.asSequence()
            .map { parsedIssue -> createIssue(parsedIssue, issuePeriodId, board, holidays) }
            .onEach { issue -> persistIssue.execute(issue) }
            .onEach { issue -> createLeadTime.execute(issue, board, holidays) }
            .toList()

        return Pair(jql, issues)
    }

    private fun createIssue(parsedIssue: ParsedIssue, issuePeriodId: Long, board: BoardEntity, holidays: List<LocalDate>): IssueEntity {
        val parsedChangelog = parsedIssue.parsedChangelog

        val leadTime = parsedIssue.startDate.daysDiff(
            parsedIssue.endDate,
            holidays,
            board.ignoreWeekend
        )

        val (
            deviationOfEstimate: Long,
            dueDateHistory: List<DueDateHistory>,
        ) = createDueDateHistory(board, parsedIssue, holidays)

        val impedimentCalculatorResult: ImpedimentCalculatorResult = calculateImpediment(parsedIssue, board, holidays)

        val efficiency = efficiencyService.calculate(
            columnChangelog = parsedChangelog.columnChangelog,
            touchingColumns = board.touchingColumns,
            waitingColumns = board.waitingColumns,
            holidays = holidays,
            ignoreWeekend = board.ignoreWeekend
        )

        return IssueEntity(
            key = parsedIssue.key,
            issueType = parsedIssue.issueType,
            creator = parsedIssue.creator,
            created = parsedIssue.created,
            startDate = parsedIssue.startDate,
            endDate = parsedIssue.endDate,
            system = parsedIssue.system,
            epic = parsedIssue.epic,
            estimate = parsedIssue.estimate,
            project = parsedIssue.project,
            summary = parsedIssue.summary,
            priority = parsedIssue.priority,
            dynamicFields = parsedIssue.dynamicFields,
            leadTime = leadTime,
            columnChangelog = parsedChangelog.columnChangelog,
            board = board,
            deviationOfEstimate = deviationOfEstimate,
            dueDateHistory = dueDateHistory,
            impedimentTime = impedimentCalculatorResult.timeInImpediment,
            impedimentHistory = impedimentCalculatorResult.impedimentHistory,
            waitTime = efficiency.waitTime,
            touchTime = efficiency.touchTime,
            pctEfficiency = efficiency.pctEfficiency,
            issuePeriodId = issuePeriodId
        )
    }

    private fun createDueDateHistory(board: BoardEntity, parsedIssue: ParsedIssue, holidays: List<LocalDate>): Pair<Long, List<DueDateHistory>> {
        val dueDateType = board.dueDateType
        val dueDateCF = board.dueDateCF

        return if (dueDateCF != null && dueDateCF.isNotEmpty() && dueDateType != null) {
            val dueDateHistory = dueDateService.parseHistory(dueDateCF, parsedIssue.parsedChangelog.fieldChangelog)
            val deviationOfEstimate = dueDateType.calcDeviationOfEstimate(dueDateHistory, parsedIssue.endDate, board.ignoreWeekend, holidays)

            Pair(deviationOfEstimate, dueDateHistory)
        } else
            Pair(0, emptyList())
    }

    private fun calculateImpediment(parsedIssue: ParsedIssue, board: BoardEntity, holidays: List<LocalDate>): ImpedimentCalculatorResult =
        board.impedimentType
            ?.calcImpediment(board.impedimentColumns, parsedIssue.parsedChangelog, parsedIssue.endDate, holidays, board.ignoreWeekend)
            ?: ImpedimentCalculatorResult()

}
