package br.com.jiratorio.controller

import br.com.jiratorio.jira.JiraProject
import br.com.jiratorio.jira.JiraProjectDetails
import br.com.jiratorio.usecase.project.FindAllJiraProjects
import br.com.jiratorio.usecase.project.FindJiraProjectDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/projects")
class ProjectController(
    private val findAllJiraProjects: FindAllJiraProjects,
    private val findJiraProjectDetails: FindJiraProjectDetails
) {

    @GetMapping
    fun findAll(): List<JiraProject> =
        findAllJiraProjects.execute()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): JiraProjectDetails =
        findJiraProjectDetails.execute(id)

}
