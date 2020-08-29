package br.com.jiratorio.usecase.board

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.DynamicFieldConfig
import br.com.jiratorio.domain.entity.Holiday
import br.com.jiratorio.domain.entity.LeadTimeConfig
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.repository.DynamicFieldConfigRepository
import br.com.jiratorio.repository.HolidayRepository
import br.com.jiratorio.repository.LeadTimeConfigRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class CloneBoard(
    private val boardRepository: BoardRepository,
    private val leadTimeConfigRepository: LeadTimeConfigRepository,
    private val holidayRepository: HolidayRepository,
    private val dynamicFieldConfigRepository: DynamicFieldConfigRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(boardId: Long): Long {
        log.info("Action=cloneBoard, boardId={}", boardId)

        val boardToClone = boardRepository.findByIdOrNull(boardId)
            ?: throw ResourceNotFound()

        val board = cloneBoard(boardToClone)
        boardRepository.save(board)

        cloneLeadTimeConfigs(boardToClone, board)
        cloneHolidays(boardToClone, board)
        cloneDynamicFields(boardToClone, board)

        return board.id
    }

    private fun cloneDynamicFields(boardToClone: Board, board: Board) {
        val dynamicFieldConfigs = boardToClone.dynamicFields?.map {
            DynamicFieldConfig(
                board = board,
                name = it.name,
                field = it.field
            )
        }

        if (!dynamicFieldConfigs.isNullOrEmpty()) {
            dynamicFieldConfigRepository.saveAll(dynamicFieldConfigs)
        }
    }

    private fun cloneHolidays(boardToClone: Board, board: Board) {
        val holidays = boardToClone.holidays?.map {
            Holiday(
                date = it.date,
                description = it.description,
                board = board
            )
        }

        if (!holidays.isNullOrEmpty()) {
            holidayRepository.saveAll(holidays)
        }
    }

    private fun cloneLeadTimeConfigs(boardToClone: Board, board: Board) {
        val leadTimeConfigs = boardToClone.leadTimeConfigs?.map {
            LeadTimeConfig(
                board = board,
                name = it.name,
                startColumn = it.startColumn,
                endColumn = it.endColumn
            )
        }
        if (!leadTimeConfigs.isNullOrEmpty()) {
            leadTimeConfigRepository.saveAll(leadTimeConfigs)
        }
    }

    private fun cloneBoard(boardToClone: Board): Board {
        return Board(
            externalId = boardToClone.externalId,
            name = boardToClone.name,
            startColumn = boardToClone.startColumn,
            endColumn = boardToClone.endColumn,
            fluxColumn = boardToClone.fluxColumn,
            ignoreIssueType = boardToClone.ignoreIssueType,
            epicCF = boardToClone.epicCF,
            estimateCF = boardToClone.estimateCF,
            systemCF = boardToClone.systemCF,
            projectCF = boardToClone.projectCF,
            dueDateCF = boardToClone.dueDateCF,
            ignoreWeekend = boardToClone.ignoreWeekend,
            impedimentType = boardToClone.impedimentType,
            impedimentColumns = boardToClone.impedimentColumns,
            touchingColumns = boardToClone.touchingColumns,
            waitingColumns = boardToClone.waitingColumns,
            dueDateType = boardToClone.dueDateType,
            useLastOccurrenceWhenCalculateLeadTime = boardToClone.useLastOccurrenceWhenCalculateLeadTime,
            issuePeriodNameFormat = boardToClone.issuePeriodNameFormat
        )
    }
}
