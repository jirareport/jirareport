package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.entity.Holiday

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HolidayRepository : CrudRepository<Holiday, Long> {

    fun findAllByBoardId(id: Long): List<Holiday>

    fun findAllByBoardId(id: Long, pageable: Pageable): Page<Holiday>

    fun findAllByBoard(board: Board): List<Holiday>

}
