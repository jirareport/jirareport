package br.com.jiratorio.usecase.board

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.request.CreateBoardRequest
import br.com.jiratorio.mapper.toBoard
import br.com.jiratorio.repository.BoardRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class CreateBoard(
    private val boardRepository: BoardRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(createBoardRequest: CreateBoardRequest): Long {
        log.info("Method=execute, createBoardRequest={}", createBoardRequest)

        val board = createBoardRequest.toBoard()
        boardRepository.save(board)

        return board.id
    }

}
