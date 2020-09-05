package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.entity.HolidayEntity

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface HolidayRepository : CrudRepository<HolidayEntity, Long> {

    fun findAllByBoardId(id: Long): List<HolidayEntity>

    fun findAllByBoardId(id: Long, pageable: Pageable): Page<HolidayEntity>

    fun findAllByBoard(board: BoardEntity): List<HolidayEntity>

    fun findByDateAndBoardId(date: LocalDate, boardId: Long): HolidayEntity?

    fun findByIdAndBoard(holidayId: Long, board: BoardEntity): HolidayEntity?

    fun findByIdOrNull(id: Long): HolidayEntity? =
        findById(id).orElse(null)

}
