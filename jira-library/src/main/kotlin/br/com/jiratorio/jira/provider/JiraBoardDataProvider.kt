package br.com.jiratorio.jira.provider

import br.com.jiratorio.domain.external.ExternalBoard
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.jira.client.ProjectClient
import br.com.jiratorio.provider.BoardDataProvider
import org.springframework.stereotype.Component

@Component
class JiraBoardDataProvider(
    private val projectClient: ProjectClient,
) : BoardDataProvider {

    override fun findAllExternalBoards(): List<ExternalBoard> =
        projectClient.findAll()

    override fun findDetails(externalId: Long): ExternalBoard =
        projectClient.findById(externalId)
            .orElseThrow(::ResourceNotFound)

    override fun findAllPossibleColumns(externalId: Long): Set<String> =
        projectClient.findStatusFromProject(externalId)
            .map { it.statuses }.flatten()
            .map { it.name }.toSet()

}
