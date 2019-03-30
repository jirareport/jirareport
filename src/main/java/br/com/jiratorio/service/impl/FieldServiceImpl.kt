package br.com.jiratorio.service.impl

import br.com.jiratorio.client.FieldClient
import br.com.jiratorio.domain.jira.JiraField
import br.com.jiratorio.extension.logger
import br.com.jiratorio.service.FieldService
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FieldServiceImpl(
    private val fieldClient: FieldClient
) : FieldService {

    private val log = logger()

    @Cacheable("findAllFields")
    @Transactional(readOnly = true)
    override fun findAllJiraFields(): List<JiraField> {
        log.info("Method=findAllJiraFields")
        return fieldClient.findAll()
    }

}
