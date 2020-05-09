package br.com.jiratorio.usecase.parse

import br.com.jiratorio.domain.entity.Board
import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDate

interface IssueParser<T> {

    fun execute(root: JsonNode, board: Board, holidays: List<LocalDate>): List<T>

}
