package br.com.jiratorio.service.issue

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.IssueEntity
import br.com.jiratorio.domain.entity.embedded.DueDateHistory
import br.com.jiratorio.domain.impediment.calculator.ImpedimentCalculatorResult
import br.com.jiratorio.domain.issue.JiraIssue
import br.com.jiratorio.mapper.toColumnChangelogEntity
import br.com.jiratorio.provider.IssueProvider
import br.com.jiratorio.repository.ColumnChangelogRepository
import br.com.jiratorio.repository.ImpedimentHistoryRepository
import br.com.jiratorio.repository.IssueRepository
import br.com.jiratorio.service.DueDateService
import br.com.jiratorio.service.EfficiencyService
import br.com.jiratorio.service.HolidayService
import br.com.jiratorio.service.LeadTimeService
import br.com.jiratorio.strategy.duedate.DueDateCalculator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class CreateIssueService(
    private val leadTimeService: LeadTimeService,
    private val holidayService: HolidayService,
    private val dueDateService: DueDateService,
    private val efficiencyService: EfficiencyService,
    private val issueProvider: IssueProvider,
    private val issueRepository: IssueRepository,
    private val impedimentHistoryRepository: ImpedimentHistoryRepository,
    private val columnChangelogRepository: ColumnChangelogRepository,
) {

    @Transactional
    fun create(board: BoardEntity, issuePeriodId: Long, startDate: LocalDate, endDate: LocalDate): Pair<String, List<IssueEntity>> {
        val holidays = holidayService.findAllDaysByBoard(board.id)

        val (jql, jiraIssues) = issueProvider.findClosedIssues(board, holidays, startDate, endDate)

        val issues = jiraIssues.asSequence()
            .map { parsedIssue -> createIssue(parsedIssue, issuePeriodId, board, holidays) }
            .onEach { issue -> persistIssue(issue) }
            .onEach { issue -> leadTimeService.create(issue, board, holidays) }
            .toList()

        return Pair(jql, issues)
    }

    private fun createIssue(jiraIssue: JiraIssue, issuePeriodId: Long, board: BoardEntity, holidays: List<LocalDate>): IssueEntity {
        val changelog = jiraIssue.changelog

        val (
            deviationOfEstimate: Long,
            dueDateHistory: List<DueDateHistory>,
        ) = createDueDateHistory(board, jiraIssue, holidays)

        val impedimentCalculatorResult: ImpedimentCalculatorResult = calculateImpediment(jiraIssue, board, holidays)

        val efficiency = efficiencyService.calculate(
            columnChangelog = changelog.columnChangelog,
            touchingColumns = board.touchingColumns,
            waitingColumns = board.waitingColumns,
            holidays = holidays,
            ignoreWeekend = board.ignoreWeekend
        )

        return IssueEntity(
            key = jiraIssue.key,
            issueType = jiraIssue.issueType,
            creator = jiraIssue.creator,
            created = jiraIssue.created,
            startDate = jiraIssue.startDate,
            endDate = jiraIssue.endDate,
            leadTime = jiraIssue.leadTime,
            system = jiraIssue.system,
            epic = jiraIssue.epic,
            estimate = jiraIssue.estimate,
            project = jiraIssue.project,
            summary = jiraIssue.summary,
            priority = jiraIssue.priority,
            dynamicFields = jiraIssue.dynamicFields,
            columnChangelog = changelog.columnChangelog.toColumnChangelogEntity(),
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

    private fun createDueDateHistory(board: BoardEntity, jiraIssue: JiraIssue, holidays: List<LocalDate>): Pair<Long, List<DueDateHistory>> {
        val dueDateType = board.dueDateType
        val dueDateCF = board.dueDateCF

        return if (dueDateCF != null && dueDateCF.isNotEmpty() && dueDateType != null) {
            val dueDateHistory = dueDateService.parseHistory(dueDateCF, jiraIssue.changelog.fieldChangelog)
            val deviationOfEstimate = DueDateCalculator.from(dueDateType)
                .calcDeviationOfEstimate(dueDateHistory, jiraIssue.endDate, board.ignoreWeekend, holidays)

            Pair(deviationOfEstimate, dueDateHistory)
        } else
            Pair(0, emptyList())
    }

    private fun calculateImpediment(jiraIssue: JiraIssue, board: BoardEntity, holidays: List<LocalDate>): ImpedimentCalculatorResult =
        board.impedimentType
            ?.calcImpediment(board.impedimentColumns, jiraIssue.changelog, jiraIssue.endDate, holidays, board.ignoreWeekend)
            ?: ImpedimentCalculatorResult()

    fun persistIssue(issue: IssueEntity): IssueEntity =
        issueRepository.save(issue)
            .also(this::saveImpedimentHistory)
            .also(this::saveColumnChangelog)

    private fun saveImpedimentHistory(issue: IssueEntity) =
        issue.impedimentHistory
            .forEach { impedimentHistory ->
                impedimentHistory.issueId = issue.id
                impedimentHistoryRepository.save(impedimentHistory)
            }

    private fun saveColumnChangelog(issue: IssueEntity) =
        issue.columnChangelog
            .forEach { columnChangelog ->
                columnChangelog.issueId = issue.id
                columnChangelogRepository.save(columnChangelog)
            }
}
