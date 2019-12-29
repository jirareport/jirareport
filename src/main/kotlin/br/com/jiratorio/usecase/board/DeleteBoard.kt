package br.com.jiratorio.usecase.board

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.repository.BoardRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class DeleteBoard(
    private val boardRepository: BoardRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun execute(id: Long, username: String) {
        log.info("Method=execute, id={}, username={}", id, username)

        val board = boardRepository.findByIdAndOwner(id, username)
            ?: throw ResourceNotFound()

        boardRepository.delete(board)
    }

}
