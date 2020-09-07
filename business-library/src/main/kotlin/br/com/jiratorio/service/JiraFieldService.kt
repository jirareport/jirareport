package br.com.jiratorio.service

import br.com.jiratorio.client.FieldClient
import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.jira.JiraField
import org.springframework.stereotype.Service

@Service
class JiraFieldService(
    private val fieldClient: FieldClient,
) {

    fun findAll(): List<JiraField> =
        fieldClient.findAll()

}
