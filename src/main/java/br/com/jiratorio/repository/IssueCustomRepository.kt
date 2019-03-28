package br.com.jiratorio.repository

import br.com.jiratorio.domain.dynamicfield.DynamicFieldsValues
import br.com.jiratorio.domain.entity.Issue
import br.com.jiratorio.domain.form.IssueForm

interface IssueCustomRepository {

    fun findByExample(boardId: Long?, issueForm: IssueForm): List<Issue>

    fun findAllDynamicFieldValues(boardId: Long?): List<DynamicFieldsValues>

}
