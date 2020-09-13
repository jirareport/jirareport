package br.com.jiratorio.service.board

import br.com.jiratorio.domain.BoardPreferences
import br.com.jiratorio.domain.entity.BoardEntity
import br.com.jiratorio.domain.request.CreateBoardRequest
import br.com.jiratorio.domain.request.SearchBoardRequest
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.domain.response.board.BoardDetailsResponse
import br.com.jiratorio.domain.response.board.BoardResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.mapper.toBoard
import br.com.jiratorio.mapper.toBoardDetailsResponse
import br.com.jiratorio.mapper.toBoardResponse
import br.com.jiratorio.mapper.updateFromUpdateBoardRequest
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.specification.SearchBoardSpecification
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardService(
    private val boardRepository: BoardRepository,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun create(createBoardRequest: CreateBoardRequest): Long {
        log.info("createBoardRequest={}", createBoardRequest)

        val board = createBoardRequest.toBoard()
        boardRepository.save(board)

        return board.id
    }

    @Transactional
    fun delete(id: Long, username: String) {
        log.info("id={}, username={}", id, username)

        val board = boardRepository.findByIdAndOwner(id, username)
            ?: throw ResourceNotFound()

        boardRepository.delete(board)
    }

    @Transactional(readOnly = true)
    fun findAll(searchBoardRequest: SearchBoardRequest, currentUser: String, pageable: Pageable): Page<BoardResponse> {
        log.info("searchBoardRequest={}, currentUser={}", searchBoardRequest, currentUser)

        val filter = SearchBoardSpecification(searchBoardRequest, currentUser)
        val boards = boardRepository.findAll(filter, pageable)

        return boards.toBoardResponse()
    }

    @Transactional(readOnly = true)
    fun findDetails(id: Long): BoardDetailsResponse {
        log.info("id={}", id)

        val board = boardRepository.findByIdOrNull(id) ?: throw ResourceNotFound()
        return board.toBoardDetailsResponse()
    }

    @Transactional
    fun findById(id: Long): BoardEntity {
        log.info("id={}", id)

        return boardRepository.findByIdOrNull(id) ?: throw ResourceNotFound()
    }

    @Transactional
    fun update(boardId: Long, updateBoardRequest: UpdateBoardRequest) {
        log.info("boardId={}, updateBoardRequest={}", boardId, updateBoardRequest)

        val board = boardRepository.findByIdOrNull(boardId) ?: throw ResourceNotFound()
        board.updateFromUpdateBoardRequest(updateBoardRequest)

        boardRepository.save(board)
    }

    @Transactional(readOnly = true)
    fun findAllOwners(currentUser: String): Set<String> {
        log.info("currentUser={}", currentUser)

        val owners = boardRepository.findAllOwners()
        return owners + currentUser
    }

    @Transactional(readOnly = true)
    fun findIssuePeriodPreferencesByBoard(boardId: Long): BoardPreferences =
        boardRepository.findIssuePeriodPreferencesByBoard(boardId) ?: throw ResourceNotFound()

}
