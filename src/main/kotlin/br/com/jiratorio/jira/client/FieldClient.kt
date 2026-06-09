package br.com.jiratorio.jira.client

import br.com.jiratorio.domain.response.FieldResponse
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange
interface FieldClient {

    @GetExchange("/rest/api/2/field")
    fun findAll(): List<FieldResponse>

}
