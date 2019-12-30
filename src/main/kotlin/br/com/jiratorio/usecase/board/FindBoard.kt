package br.com.jiratorio.usecase.board

import br.com.jiratorio.config.stereotype.UseCase
import br.com.jiratorio.domain.response.board.BoardDetailsResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.mapper.toBoardDetailsResponse
import br.com.jiratorio.repository.BoardRepository
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindBoard(
    private val boardRepository: BoardRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(id: Long): BoardDetailsResponse {
        log.info("Method=execute, id={}", id)

        val board = boardRepository.findByIdOrNull(id) ?: throw ResourceNotFound()
        return board.toBoardDetailsResponse()
    }

}
