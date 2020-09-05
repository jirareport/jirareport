package br.com.jiratorio.usecase.project

import br.com.jiratorio.client.ProjectClient
import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.repository.BoardRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindJiraProjectStatusesUseCase(
    private val projectClient: ProjectClient,
    private val boardRepository: BoardRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(boardId: Long): Set<String> {
        log.info("Action=findJiraProjectStatuses, boardId={}", boardId)

        val board = boardRepository.findByIdOrNull(boardId) ?: throw ResourceNotFound()
        return projectClient.findStatusFromProject(board.externalId)
            .map { it.statuses }.flatten()
            .map { it.name }.toSet()
    }

}
