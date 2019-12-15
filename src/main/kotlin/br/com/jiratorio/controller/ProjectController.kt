package br.com.jiratorio.controller

import br.com.jiratorio.domain.jira.JiraProjectDetails
import br.com.jiratorio.domain.jira.JiraProject
import br.com.jiratorio.service.ProjectService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/projects")
class ProjectController(private val projectService: ProjectService) {

    @GetMapping
    fun findAll(): List<JiraProject> =
        projectService.findAllJiraProject()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): JiraProjectDetails =
        projectService.findById(id)

}
