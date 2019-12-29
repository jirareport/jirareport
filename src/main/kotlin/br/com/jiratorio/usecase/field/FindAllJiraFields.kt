package br.com.jiratorio.usecase.field

import br.com.jiratorio.client.FieldClient
import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.jira.JiraField
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable

@UseCase
class FindAllJiraFields(
    private val fieldClient: FieldClient
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Cacheable("findAllFields")
    fun execute(): List<JiraField> {
        log.info("Method=execute")
        return fieldClient.findAll()
    }

}
