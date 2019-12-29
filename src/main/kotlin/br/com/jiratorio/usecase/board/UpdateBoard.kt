package br.com.jiratorio.usecase.board

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.mapper.updateFromUpdateBoardRequest
import br.com.jiratorio.repository.BoardRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class UpdateBoard(
    private val boardRepository: BoardRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(boardId: Long, updateBoardRequest: UpdateBoardRequest) {
        log.info("Method=execute, boardId={}, updateBoardRequest={}", boardId, updateBoardRequest)

        val board = boardRepository.findByIdOrNull(boardId) ?: throw ResourceNotFound()
        board.updateFromUpdateBoardRequest(updateBoardRequest)

        boardRepository.save(board)
    }

}
