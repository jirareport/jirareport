package br.com.jiratorio.usecase.leadtime

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.entity.LeadTime
import br.com.jiratorio.domain.entity.LeadTimeConfig
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.time.daysDiff
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.repository.LeadTimeConfigRepository
import br.com.jiratorio.repository.LeadTimeRepository
import br.com.jiratorio.usecase.holiday.FindHolidayDays
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@UseCase
class CreateLeadTime(
    private val leadTimeConfigRepository: LeadTimeConfigRepository,
    private val leadTimeRepository: LeadTimeRepository,
    private val boardRepository: BoardRepository,
    private val findHolidayDays: FindHolidayDays
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(issues: List<Issue>, boardId: Long) {
        log.info("Action=createLeadTime, issues={}, boardId={}", issues, boardId)

        val leadTimeConfigs = leadTimeConfigRepository.findByBoardId(boardId)

        if (leadTimeConfigs.isEmpty()) {
            return
        }

        val holidays = findHolidayDays.execute(boardId)
        val board = boardRepository.findByIdOrNull(boardId) ?: throw ResourceNotFound()

        issues.forEach { issue ->
            leadTimeRepository.deleteByIssueId(issue.id)
            issue.leadTimes = leadTimeConfigs.mapNotNull { leadTimeConfig ->
                calcLeadTime(leadTimeConfig, issue, holidays, board)
            }.toMutableSet()
        }
    }

    private fun calcLeadTime(
        leadTimeConfig: LeadTimeConfig,
        issue: Issue,
        holidays: List<LocalDate>,
        board: Board
    ): LeadTime? {
        log.info(
            "Method=calcLeadTime, leadTimeConfig={}, issue={}, holidays={}, board={}",
            leadTimeConfig, issue, holidays, board
        )

        val fluxColumn = FluxColumn(
            startLeadTimeColumn = leadTimeConfig.startColumn,
            endLeadTimeColumn = leadTimeConfig.endColumn,
            orderedColumns = board.fluxColumn,
            useLastOccurrenceWhenCalculateLeadTime = board.useLastOccurrenceWhenCalculateLeadTime
        )
        val (startDate, endDate) = fluxColumn.calcStartAndEndDate(issue.changelog, issue.created)

        if (startDate == null || endDate == null) {
            return null
        }

        val leadTime = startDate.daysDiff(endDate, holidays, board.ignoreWeekend)

        return leadTimeRepository.save(
            LeadTime(
                leadTimeConfig = leadTimeConfig,
                issue = issue,
                leadTime = leadTime,
                startDate = startDate,
                endDate = endDate
            )
        )
    }
}
