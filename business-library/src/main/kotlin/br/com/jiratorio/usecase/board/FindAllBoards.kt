package br.com.jiratorio.usecase.board

import br.com.jiratorio.stereotype.UseCase
import br.com.jiratorio.domain.request.SearchBoardRequest
import br.com.jiratorio.domain.response.board.BoardResponse
import br.com.jiratorio.mapper.toBoardResponse
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.specification.SearchBoardSpecification
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional

@UseCase
class FindAllBoards(
    private val boardRepository: BoardRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    fun execute(
        searchBoardRequest: SearchBoardRequest,
        currentUser: String,
        pageable: Pageable
    ): Page<BoardResponse> {
        log.info("Action=findAllBoards, searchBoardRequest={}, currentUser={}", searchBoardRequest, currentUser)

        val filter = SearchBoardSpecification(searchBoardRequest, currentUser)
        val boards = boardRepository.findAll(filter, pageable)

        return boards.toBoardResponse()
    }

}
