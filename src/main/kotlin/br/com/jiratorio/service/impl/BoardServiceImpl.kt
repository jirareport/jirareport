package br.com.jiratorio.service.impl

import br.com.jiratorio.aspect.annotation.ExecutionTime
import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.request.CreateBoardRequest
import br.com.jiratorio.domain.request.SearchBoardRequest
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.domain.response.board.BoardDetailsResponse
import br.com.jiratorio.domain.response.board.BoardResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.log
import br.com.jiratorio.mapper.toBoard
import br.com.jiratorio.mapper.toBoardDetailsResponse
import br.com.jiratorio.mapper.toBoardResponse
import br.com.jiratorio.mapper.updateFromUpdateBoardRequest
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.specification.SearchBoardSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardServiceImpl(
    private val boardRepository: BoardRepository
) : BoardService {

    @Transactional(readOnly = true)
    override fun findAll(
        pageable: Pageable,
        searchBoardRequest: SearchBoardRequest,
        currentUser: Account
    ): Page<BoardResponse> {
        log.info("Method=findAll, searchBoardRequest={}, currentUser={}", searchBoardRequest, currentUser)

        val filter = SearchBoardSpecification(searchBoardRequest, currentUser)
        val boards = boardRepository.findAll(filter, pageable)

        return boards.toBoardResponse()
    }

    @Transactional
    override fun create(createBoardRequest: CreateBoardRequest): Long {
        log.info("Method=create, createBoardRequest={}", createBoardRequest)

        val board = createBoardRequest.toBoard()
        boardRepository.save(board)

        return board.id
    }

    @Transactional
    override fun delete(id: Long, username: String) {
        log.info("Method=delete, id={}, username={}", id, username)

        val board = boardRepository.findByIdAndOwner(id, username)
            ?: throw ResourceNotFound()

        boardRepository.delete(board)
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): Board {
        log.info("Method=findByBoardAndId, id={}", id)

        return boardRepository.findByIdOrNull(id)
            ?: throw ResourceNotFound()
    }

    @Transactional
    override fun update(boardId: Long, updateBoardRequest: UpdateBoardRequest) {
        log.info("Method=board, updateBoardRequest={}", updateBoardRequest)

        val board = findById(boardId)
        board.updateFromUpdateBoardRequest(updateBoardRequest)

        boardRepository.save(board)
    }

    @Transactional(readOnly = true)
    override fun findDetailsById(id: Long): BoardDetailsResponse {
        log.info("Method=findDetailsById, id={}", id)

        return findById(id).toBoardDetailsResponse()
    }

    @ExecutionTime
    @Transactional(readOnly = true)
    override fun findAllOwners(currentUser: Account): Set<String> {
        log.info("Method=findAllOwners, currentUser={}", currentUser)

        val owners = boardRepository.findAllOwners()
        return owners + currentUser.username
    }

}
