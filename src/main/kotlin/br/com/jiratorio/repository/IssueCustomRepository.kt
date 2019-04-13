package br.com.jiratorio.repository

import br.com.jiratorio.domain.dynamicfield.DynamicFieldsValues
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.request.SearchIssueRequest

interface IssueCustomRepository {

    fun findByExample(boardId: Long, searchIssueRequest: SearchIssueRequest): List<Issue>

    fun findAllDynamicFieldValues(boardId: Long): List<DynamicFieldsValues>

}
