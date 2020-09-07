package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.DynamicFieldConfigEntity
import br.com.jiratorio.domain.entity.HolidayEntity
import br.com.jiratorio.domain.entity.LeadTimeConfigEntity
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.repository.DynamicFieldConfigRepository
import br.com.jiratorio.repository.HolidayRepository
import br.com.jiratorio.repository.LeadTimeConfigRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CloneBoardService(
    private val boardRepository: BoardRepository,
    private val leadTimeConfigRepository: LeadTimeConfigRepository,
    private val holidayRepository: HolidayRepository,
    private val dynamicFieldConfigRepository: DynamicFieldConfigRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun clone(boardId: Long): Long {
        log.info("boardId={}", boardId)

        val boardToClone = boardRepository.findByIdOrNull(boardId)
            ?: throw ResourceNotFound()

        val board = cloneBoard(boardToClone)
        boardRepository.save(board)

        cloneLeadTimeConfigs(boardToClone, board)
        cloneHolidays(boardToClone, board)
        cloneDynamicFields(boardToClone, board)

        return board.id
    }

    private fun cloneDynamicFields(boardToClone: BoardEntity, board: BoardEntity) {
        val dynamicFieldConfigs = boardToClone.dynamicFields?.map {
            DynamicFieldConfigEntity(
                board = board,
                name = it.name,
                field = it.field
            )
        }

        if (!dynamicFieldConfigs.isNullOrEmpty()) {
            dynamicFieldConfigRepository.saveAll(dynamicFieldConfigs)
        }
    }

    private fun cloneHolidays(boardToClone: BoardEntity, board: BoardEntity) {
        val holidays = boardToClone.holidays?.map {
            HolidayEntity(
                date = it.date,
                description = it.description,
                board = board
            )
        }

        if (!holidays.isNullOrEmpty()) {
            holidayRepository.saveAll(holidays)
        }
    }

    private fun cloneLeadTimeConfigs(boardToClone: BoardEntity, board: BoardEntity) {
        val leadTimeConfigs = boardToClone.leadTimeConfigs?.map {
            LeadTimeConfigEntity(
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

    private fun cloneBoard(boardToClone: BoardEntity): BoardEntity {
        return BoardEntity(
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
