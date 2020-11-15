package br.com.jiratorio.provider

import br.com.jiratorio.domain.issue.FindIssueResult
import br.com.jiratorio.domain.entity.BoardEntity
import java.time.LocalDate

interface IssueProvider {

    fun findClosedIssues(board: BoardEntity, holidays: List<LocalDate>, startDate: LocalDate, endDate: LocalDate): FindIssueResult

    fun findOpenIssues(board: BoardEntity, holidays: List<LocalDate>): FindIssueResult

}
