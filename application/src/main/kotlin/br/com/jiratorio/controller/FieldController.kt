package br.com.jiratorio.controller

import br.com.jiratorio.jira.JiraField
import br.com.jiratorio.service.JiraFieldService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/fields")
class FieldController(
    private val jiraFieldService: JiraFieldService
) {

    @GetMapping
    fun fields(): List<JiraField> =
        jiraFieldService.findAll()

}
