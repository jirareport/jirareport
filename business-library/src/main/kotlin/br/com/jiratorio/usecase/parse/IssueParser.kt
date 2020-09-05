package br.com.jiratorio.usecase.parse

import br.com.jiratorio.domain.entity.BoardEntity
import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDate

interface IssueParser<T> {

    fun execute(root: JsonNode, board: BoardEntity, holidays: List<LocalDate>): List<T>

}
