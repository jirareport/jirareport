package br.com.jiratorio.client

import br.com.jiratorio.config.AuthClientConfiguration
import br.com.jiratorio.jira.JiraUser
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "auth-client",
    url = "\${jira.url}",
    configuration = [
        AuthClientConfiguration::class
    ]
)
interface AuthClient {

    @GetMapping("/rest/api/2/myself")
    fun login(@RequestHeader("Authorization") token: String): JiraUser

}
