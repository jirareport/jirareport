package br.com.jiratorio.usecase.issue.create

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.domain.impediment.calculator.ImpedimentCalculatorResult
import br.com.jiratorio.domain.parsed.ParsedIssue
import br.com.jiratorio.extension.time.daysDiff
import br.com.jiratorio.usecase.duedate.CreateDueDateHistory
import br.com.jiratorio.usecase.efficiency.CalculateEfficiency
import br.com.jiratorio.usecase.holiday.FindHolidayDays
import br.com.jiratorio.usecase.leadtime.CreateLeadTime
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@UseCase
class CreateIssues(
    private val findAllOpenIssues: FindAllOpenIssues,
    private val createLeadTime: CreateLeadTime,
    private val findHolidayDays: FindHolidayDays,
    private val createDueDateHistory: CreateDueDateHistory,
    private val calculateEfficiency: CalculateEfficiency,
    private val persistIssue: PersistIssue
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(board: Board, issuePeriodId: Long, startDate: LocalDate, endDate: LocalDate): Pair<String, List<Issue>> {
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

    private fun createIssue(parsedIssue: ParsedIssue, issuePeriodId: Long, board: Board, holidays: List<LocalDate>): Issue {
        val parsedChangelog = parsedIssue.parsedChangelog

        val leadTime = parsedIssue.startDate.daysDiff(
            parsedIssue.endDate,
            holidays,
            board.ignoreWeekend
        )

        val (
            deviationOfEstimate: Long,
            dueDateHistory: List<DueDateHistory>
        ) = createDueDateHistory(board, parsedIssue, holidays)

        val impedimentCalculatorResult: ImpedimentCalculatorResult = calculateImpediment(parsedIssue, board, holidays)

        val efficiency = calculateEfficiency.execute(
            columnChangelog = parsedChangelog.columnChangelog,
            touchingColumns = board.touchingColumns,
            waitingColumns = board.waitingColumns,
            holidays = holidays,
            ignoreWeekend = board.ignoreWeekend
        )

        return Issue(
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

    private fun createDueDateHistory(board: Board, parsedIssue: ParsedIssue, holidays: List<LocalDate>): Pair<Long, List<DueDateHistory>> {
        val dueDateType = board.dueDateType
        val dueDateCF = board.dueDateCF

        return if (dueDateCF != null && dueDateCF.isNotEmpty() && dueDateType != null) {
            val dueDateHistory = createDueDateHistory.execute(dueDateCF, parsedIssue.parsedChangelog.fieldChangelog)
            val deviationOfEstimate = dueDateType.calcDeviationOfEstimate(dueDateHistory, parsedIssue.endDate, board.ignoreWeekend, holidays)

            Pair(deviationOfEstimate, dueDateHistory)
        } else
            Pair(0, emptyList())
    }

    private fun calculateImpediment(parsedIssue: ParsedIssue, board: Board, holidays: List<LocalDate>): ImpedimentCalculatorResult =
        board.impedimentType?.calcImpediment(
            board.impedimentColumns,
            parsedIssue.parsedChangelog,
            parsedIssue.endDate,
            holidays,
            board.ignoreWeekend
        ) ?: ImpedimentCalculatorResult()

}
