package br.com.jiratorio.usecase.project

import br.com.jiratorio.client.ProjectClient
import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.usecase.board.FindBoard
import org.slf4j.LoggerFactory

@UseCase
class FindProjectStatusesByBoard(
    private val projectClient: ProjectClient,
    private val findBoard: FindBoard
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun execute(boardId: Long): Set<String> {
        log.info("Method=findStatusesByBoardId, boardId={}", boardId)

        val board = findBoard.execute(boardId)
        return projectClient.findStatusFromProject(board.externalId)
            .map { it.statuses }.flatten()
            .map { it.name }.toSet()
    }

}
