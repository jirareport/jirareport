package br.com.jiratorio.service.impl

import br.com.jiratorio.aspect.annotation.ExecutionTime
import br.com.jiratorio.domain.Account
import br.com.jiratorio.domain.entity.Board
import br.com.jiratorio.domain.request.CreateBoardRequest
import br.com.jiratorio.domain.request.SearchBoardRequest
import br.com.jiratorio.domain.request.UpdateBoardRequest
import br.com.jiratorio.domain.response.BoardDetailsResponse
import br.com.jiratorio.domain.response.BoardResponse
import br.com.jiratorio.exception.ResourceNotFound
import br.com.jiratorio.extension.logger
import br.com.jiratorio.mapper.BoardMapper
import br.com.jiratorio.repository.BoardRepository
import br.com.jiratorio.service.BoardService
import br.com.jiratorio.service.ProjectService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.criteria.Predicate

@Service
class BoardServiceImpl(
    private val projectService: ProjectService,
    private val boardRepository: BoardRepository,
    private val boardMapper: BoardMapper
) : BoardService {

    private val log = logger()

    @Transactional(readOnly = true)
    override fun findAll(
        pageable: Pageable,
        searchBoardRequest: SearchBoardRequest,
        currentUser: Account
    ): Page<BoardResponse> {
        log.info("Method=findAll, searchBoardRequest={}, currentUser={}", searchBoardRequest, currentUser)

        val filter = Specification<Board> { from, query, builder ->
            val predicates: MutableList<Predicate> = ArrayList()

            if (searchBoardRequest.name != null) {
                predicates.add(
                    builder.like(builder.lower(from.get<String>("name")), "%${searchBoardRequest.name}%".toLowerCase())
                )
            }

            if (searchBoardRequest.owner == null) {
                predicates.add(builder.equal(from.get<String>("owner"), currentUser.username))
            } else if (searchBoardRequest.owner != "all") {
                predicates.add(builder.equal(from.get<String>("owner"), searchBoardRequest.owner))
            }

            builder.and(*predicates.toTypedArray())
        }

        val boards = boardRepository.findAll(filter, pageable)
        return boardMapper.toBoardResponse(boards)
    }

    @Transactional
    override fun create(createBoardRequest: CreateBoardRequest): Long {
        log.info("Method=create, createBoardRequest={}", createBoardRequest)

        val board = boardMapper.boardFromCreateBoardRequest(createBoardRequest)
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
        log.info("Method=findById, id={}", id)

        return boardRepository.findByIdOrNull(id)
            ?: throw ResourceNotFound()
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
