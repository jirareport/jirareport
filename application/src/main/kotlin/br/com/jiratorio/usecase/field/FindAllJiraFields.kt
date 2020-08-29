package br.com.jiratorio.usecase.field

import br.com.jiratorio.client.FieldClient
import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.jira.JiraField
import org.springframework.cache.annotation.Cacheable

@UseCase
class FindAllJiraFields(
    private val fieldClient: FieldClient
) {

    @Cacheable("findAllFields")
    fun execute(): List<JiraField> {
        return fieldClient.findAll()
    }

}
