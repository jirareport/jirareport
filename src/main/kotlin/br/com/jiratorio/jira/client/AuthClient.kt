package br.com.jiratorio.jira.client

import br.com.jiratorio.jira.domain.JiraUser
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange
interface AuthClient {

    @GetExchange("/rest/api/2/myself")
    fun login(@RequestHeader("Authorization") token: String): JiraUser

}
