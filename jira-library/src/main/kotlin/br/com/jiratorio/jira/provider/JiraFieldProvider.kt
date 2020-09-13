package br.com.jiratorio.jira.provider

import br.com.jiratorio.domain.response.FieldResponse
import br.com.jiratorio.jira.client.FieldClient
import br.com.jiratorio.provider.FieldProvider
import org.springframework.stereotype.Component

@Component
class JiraFieldProvider(
    private val fieldClient: FieldClient
) : FieldProvider {

    override fun findAll(): List<FieldResponse> =
        fieldClient.findAll()

}
