package br.com.jiratorio.usecase.leadtime

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.FluxColumn
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.IssueEntity
import br.com.jiratorio.domain.entity.LeadTimeEntity
import br.com.jiratorio.domain.entity.LeadTimeConfigEntity
import br.com.jiratorio.extension.time.daysDiff
import br.com.jiratorio.repository.LeadTimeConfigRepository
import br.com.jiratorio.repository.LeadTimeRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@UseCase
class CreateLeadTime(
    private val leadTimeConfigRepository: LeadTimeConfigRepository,
    private val leadTimeRepository: LeadTimeRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(issue: IssueEntity, board: BoardEntity, holidays: List<LocalDate>) {
        log.info("Action=createLeadTime, issue={}, board={}", issue, board)

        val leadTimeConfigs = leadTimeConfigRepository.findByBoardId(board.id)

        if (leadTimeConfigs.isEmpty()) {
            return
        }

        leadTimeRepository.deleteByIssueId(issue.id)
        issue.leadTimes = leadTimeConfigs
            .mapNotNull { leadTimeConfig -> calcLeadTime(leadTimeConfig, issue, board, holidays) }
            .toMutableSet()
    }

    private fun calcLeadTime(
        leadTimeConfig: LeadTimeConfigEntity,
        issue: IssueEntity,
        board: BoardEntity,
        holidays: List<LocalDate>,
    ): LeadTimeEntity? {
        log.info("Method=calcLeadTime, leadTimeConfig={}, issue={}, board={},  holidays={}", leadTimeConfig, issue, board, holidays)

        val fluxColumn = FluxColumn(
            startLeadTimeColumn = leadTimeConfig.startColumn,
            endLeadTimeColumn = leadTimeConfig.endColumn,
            orderedColumns = board.fluxColumn,
            useLastOccurrenceWhenCalculateLeadTime = board.useLastOccurrenceWhenCalculateLeadTime
        )
        val (startDate, endDate) = fluxColumn.calcStartAndEndDate(issue.columnChangelog, issue.created)

        if (startDate == null || endDate == null) {
            return null
        }

        val leadTime = startDate.daysDiff(endDate, holidays, board.ignoreWeekend)

        return leadTimeRepository.save(
            LeadTimeEntity(
                leadTimeConfig = leadTimeConfig,
                issue = issue,
                leadTime = leadTime,
                startDate = startDate,
                endDate = endDate
            )
        )
    }
}
