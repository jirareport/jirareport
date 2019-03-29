package br.com.jiratorio.service

import br.com.jiratorio.domain.entity.Board
import java.time.LocalDate

interface JQLService {

    fun finalizedIssues(board: Board, start: LocalDate, end: LocalDate): String

    fun openedIssues(board: Board): String

}
