package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Holiday

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface HolidayRepository : CrudRepository<Holiday, Long> {

    fun findAllByBoardId(id: Long): List<Holiday>

    fun findAllByBoardId(id: Long, pageable: Pageable): Page<Holiday>

    fun findAllByBoard(board: Board): List<Holiday>

    fun findByDateAndBoardId(date: LocalDate, boardId: Long): Holiday?

}
