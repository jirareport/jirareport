package br.com.jiratorio.service

import br.com.jiratorio.domain.external.ExternalBoard
import br.com.jiratorio.provider.BoardDataProvider
import br.com.jiratorio.service.board.BoardService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExternalBoardService(
    private val boardService: BoardService,
    private val boardDataProvider: BoardDataProvider,
) {

    @Transactional(readOnly = true)
    fun findAll(): List<ExternalBoard> =
        boardDataProvider.findAllExternalBoards()

    @Transactional(readOnly = true)
    fun findById(id: Long): ExternalBoard =
        boardDataProvider.findDetails(id)

    @Transactional(readOnly = true)
    fun findAllPossibleColumns(boardId: Long): Set<String> {
        val board = boardService.findById(boardId)
        return boardDataProvider.findAllPossibleColumns(board.externalId)
    }

}
