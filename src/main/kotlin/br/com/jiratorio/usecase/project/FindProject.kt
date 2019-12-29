package br.com.jiratorio.usecase.project

import br.com.jiratorio.client.ProjectClient
import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.jira.JiraProjectDetails
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.log

@UseCase
class FindProject(
    private val projectClient: ProjectClient
) {

    fun execute(projectId: Long): JiraProjectDetails {
        log.info("Method=execute, projectId={}", projectId)

        return projectClient.findById(projectId)
            .orElseThrow { ResourceNotFound() }
    }

}
