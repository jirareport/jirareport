package br.com.jiratorio.usecase.field

import br.com.jiratorio.client.FieldClient
import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.jira.JiraField

@UseCase
class FindAllJiraFields(
    private val fieldClient: FieldClient
) {

    fun execute(): List<JiraField> {
        return fieldClient.findAll()
    }

}
