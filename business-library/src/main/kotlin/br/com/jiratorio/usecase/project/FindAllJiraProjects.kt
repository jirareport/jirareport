package br.com.jiratorio.usecase.project

import br.com.jiratorio.client.ProjectClient
import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.jira.JiraProject

@UseCase
class FindAllJiraProjects(
    private val projectClient: ProjectClient
) {

    fun execute(): List<JiraProject> {
        return projectClient.findAll()
    }

}
