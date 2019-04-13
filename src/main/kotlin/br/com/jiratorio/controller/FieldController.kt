package br.com.jiratorio.controller

import br.com.jiratorio.domain.jira.JiraField
import br.com.jiratorio.service.FieldService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/fields")
class FieldController(private val fieldService: FieldService) {

    @GetMapping
    fun fields(): List<JiraField> {
        return fieldService.findAllJiraFields()
    }

}
