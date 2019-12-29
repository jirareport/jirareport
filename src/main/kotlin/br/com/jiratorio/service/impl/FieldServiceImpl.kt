package br.com.jiratorio.service.impl

import br.com.jiratorio.domain.jira.JiraField
import br.com.jiratorio.extension.log
import br.com.jiratorio.service.FieldService
import br.com.jiratorio.usecase.field.FindAllJiraFields
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FieldServiceImpl(
    private val findAllJiraFields: FindAllJiraFields
) : FieldService {

    @Cacheable("findAllFields")
    @Transactional(readOnly = true)
    override fun findAllJiraFields(): List<JiraField> {
        log.info("Method=findAllJiraFields")
        return findAllJiraFields.execute()
    }

}
