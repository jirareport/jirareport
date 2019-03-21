package br.com.jiratorio.service.impl

import br.com.jiratorio.aspect.annotation.ExecutionTime
import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.request.CreateBoardRequest
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.domain.response.BoardDetailsResponse
import br.com.jiratorio.domain.response.BoardResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.mapper.BoardMapper
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.ProjectService
import br.com.jiratorio.extension.logger
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils

@Service
class BoardServiceImpl(
    private val projectService: ProjectService,
    private val boardRepository: BoardRepository,
    private val boardMapper: BoardMapper
) : BoardService {

    private val log = logger()

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, board: Board, currentUser: Account): Page<BoardResponse> {
        log.info("Method=findAll, board={}, currentUser={}", board, currentUser)

        if (StringUtils.isEmpty(board.owner)) {
            board.owner = currentUser.username
        }

        if ("all" == board.owner) {
            board.owner = null
        }

        val matcher = ExampleMatcher.matching()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
            .withMatcher("owner", ExampleMatcher.GenericPropertyMatchers.exact())
            .withIgnoreNullValues()
            .withIgnoreCase()

        val example = Example.of(board, matcher)

        val boards = boardRepository.findAll(example, pageable)
        return boardMapper.toBoardResponse(boards)
    }

    @Transactional
    override fun create(createBoardRequest: CreateBoardRequest): Long {
        log.info("Method=create, createBoardRequest={}", createBoardRequest)

        val board = boardMapper.boardFromCreateBoardRequest(createBoardRequest)
        boardRepository.save(board)

        return board.id!!
    }

    @Transactional
    override fun delete(id: Long, username: String) {
        log.info("Method=delete, id={}, username={}", id, username)

        val board = boardRepository.findByIdAndOwner(id, username)
            .orElseThrow(::ResourceNotFound)
        boardRepository.delete(board)
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): Board {
        log.info("Method=findById, id={}", id)

        return boardRepository.findById(id)
            .orElseThrow(::ResourceNotFound)
    }

    @Transactional
    override fun update(boardId: Long, updateBoardRequest: UpdateBoardRequest) {
        log.info("Method=board, updateBoardRequest={}", updateBoardRequest)

        val board = findById(boardId)
        boardMapper.fromUpdateBoardRequest(board, updateBoardRequest)

        boardRepository.save(board)
    }

    @Transactional(readOnly = true)
    override fun findStatusFromBoardInJira(boardId: Long): Set<String> {
        val board = findById(boardId)
        return findStatusFromBoardInJira(board)
    }

    @Transactional(readOnly = true)
    override fun findStatusFromBoardInJira(board: Board): Set<String> {
        log.info("Method=findStatusFromBoardInJira, board={}", board)

        val listStatusesBoard = projectService.findStatusFromProject(board.externalId)
        return listStatusesBoard
            .map { it.statuses }.flatten()
            .map { it.name }.toSet()
    }

    @Transactional(readOnly = true)
    override fun findDetailsById(id: Long): BoardDetailsResponse {
        log.info("Method=findDetailsById, id={}", id)
        return boardMapper.toBoardResponseDetails(findById(id))
    }

    @ExecutionTime
    @Transactional(readOnly = true)
    override fun findAllOwners(): Set<String> {
        log.info("Method=findAllOwners")
        return boardRepository.findAllOwners()
    }
}
